package cn.iwakeup.r2client.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.data.PreferenceRepository
import cn.iwakeup.r2client.data.PreferencesDataStore
import cn.iwakeup.r2client.toBasicInfo
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


sealed interface AppWrapperUIState {
    object Loading : AppWrapperUIState
    data class Success(val buckets: List<BucketBasicInfo>) : AppWrapperUIState
}

@Suppress("UNCHECKED_CAST")
class AppWrapperViewModel : ViewModel() {
    val preferenceRepository = PreferenceRepository(PreferencesDataStore.instance)

    private val _uiStat = MutableStateFlow<AppWrapperUIState>(AppWrapperUIState.Loading)
    val uiState: StateFlow<AppWrapperUIState> = _uiStat


    private val _loadedBuckets = mutableStateListOf<BucketBasicInfo>()
    val loadedBuckets: SnapshotStateList<BucketBasicInfo> = _loadedBuckets


    fun loadBuckets() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiStat.emit(AppWrapperUIState.Loading)
            delay(1000)
            val buckets = async { R2Client.get().listBuckets() }.await()
            preferenceRepository.getBucketsPublicURL(buckets).collect { urls ->
                val basicBuckets = buckets.mapIndexed { index, rawBucket ->
                    rawBucket.toBasicInfo(urls[index])
                }
                _loadedBuckets.apply {
                    clear()
                    _loadedBuckets.addAll(basicBuckets)
                }
                _uiStat.emit(AppWrapperUIState.Success(basicBuckets))
            }
        }
    }


    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: KClass<T>,
            extras: CreationExtras
        ): T {
            return AppWrapperViewModel() as T
        }
    }
}
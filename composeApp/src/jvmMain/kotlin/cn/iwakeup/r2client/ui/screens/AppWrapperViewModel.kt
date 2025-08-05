package cn.iwakeup.r2client.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.toBasicInfo
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed interface AppWrapperUIState {
    object Loading : AppWrapperUIState
    data class Success(val buckets: List<BucketBasicInfo>) : AppWrapperUIState
}

class AppWrapperViewModel : ViewModel() {


    private val _uiStat = MutableStateFlow<AppWrapperUIState>(AppWrapperUIState.Loading)
    val uiState: StateFlow<AppWrapperUIState> = _uiStat


    fun loadBuckets() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiStat.emit(AppWrapperUIState.Loading)
            delay(3000)
            val buckets = R2Client.get().listBuckets().map { it.toBasicInfo() }
            _uiStat.emit(AppWrapperUIState.Success(buckets))
        }
    }


}
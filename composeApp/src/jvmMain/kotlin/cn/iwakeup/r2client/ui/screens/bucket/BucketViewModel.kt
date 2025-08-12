package cn.iwakeup.r2client.ui.screens.bucket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.domain.SystemToolkit
import com.iwakeup.r2client.api.R2Client
import com.iwakeup.r2client.data.BucketFullInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlin.reflect.KClass


@Suppress("UNCHECKED_CAST")
class BucketViewModel() : ViewModel() {


    private val _uiState = MutableStateFlow(BucketScreenUIState(true, emptyList()))
    val uiState: StateFlow<BucketScreenUIState> = _uiState


    suspend fun getBucketsInfo(bucketBasicList: List<BucketBasicInfo>) {

        // will not get all objects each time that display BucketScreen
        // only triggered by manual refresh or initial loading
        if (uiState.value.bucketFullInfoList.isNotEmpty()) {
            return
        }


        flow {
            bucketBasicList.forEachIndexed { index, bucket ->
                val objectList = R2Client.get().listObjects(bucket.bucketName)
                emit(BucketFullInfo(index, bucket.bucketName, bucket.createTime, objectList, bucket.publicURL))
            }

        }.collect { bucketFullInfo ->

            val isLast = bucketFullInfo.index == bucketBasicList.lastIndex

            _uiState.update { current ->
                current.copy(isLoading = !isLast, bucketFullInfoList = current.bucketFullInfoList + bucketFullInfo)
            }


        }
    }


    fun copyObjectLink(bucketFullInfo: BucketFullInfo, objectKey: String) {
        SystemToolkit.copyObjectLink(bucketFullInfo, objectKey)
    }

    fun deleteObject(bucketFullInfo: BucketFullInfo, objectKey: String) {

    }


    companion object Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: KClass<T>,
            extras: CreationExtras
        ): T {
            return BucketViewModel() as T
        }
    }

}
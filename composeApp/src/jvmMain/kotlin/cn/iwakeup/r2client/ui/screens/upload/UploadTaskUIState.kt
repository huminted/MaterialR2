package cn.iwakeup.r2client.ui.screens.upload

import androidx.compose.runtime.MutableState
import com.iwakeup.r2client.api.UploadTask

data class UploadTaskUIState(
    val task: UploadTask,
    var process: MutableState<Double>,
    val status: MutableState<UploadTaskStatus>
)

enum class UploadTaskStatus {
    Pending,
    Progressing,
    Finished
}
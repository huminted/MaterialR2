package cn.iwakeup.r2client.ui.screens.upload

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.data.UploadingJob
import cn.iwakeup.r2client.domain.SystemToolkit
import cn.iwakeup.r2client.stateOf
import cn.iwakeup.r2client.toImmutable
import cn.iwakeup.r2client.toUploadTask
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel : ViewModel() {


    private val _fileState = mutableStateOf<File?>(null)
    val fileState = _fileState.toImmutable()

    private var _fileList = mutableStateListOf<UploadTaskUIState>()
    var fileList: SnapshotStateList<UploadTaskUIState> = _fileList


    private val _selectedBucket = mutableStateOf<BucketBasicInfo?>(null)
    val selectedBucket: State<BucketBasicInfo?> = _selectedBucket

    private val uploadingJobsMap = mutableMapOf<String, UploadingJob>()


    fun selectBucket(bucket: BucketBasicInfo) {
        _selectedBucket.value = bucket
    }

    fun dropFile(file: File) {
        _fileState.value = file
        _fileList.clear()

        if (file.isFile) {
            _fileList.add(file.toUploadTask().stateOf())
        } else if (file.isDirectory) {
            val filesInDir = SystemToolkit.listAllFilesInFolder(file).map { it.toUploadTask().stateOf() }.toList()
            _fileList.addAll(filesInDir)
        }


    }

    fun removeFile(file: File) {
        val target = fileList.firstOrNull { it.task.taskId == file.absolutePath }
        if (target != null) {
            fileList.remove(target)
            cancelUploadingTask(file.absolutePath)
        }
    }

    fun copyLink(publicURL: String, objectKey: String) {

        SystemToolkit.copyObjectLink(publicURL, objectKey)
    }

    fun uploadToR2() {
        if (selectedBucket.value == null) {
            return
        }

        viewModelScope.launch {
            fileList.forEach { taskState ->
                val job = async(Dispatchers.IO) {
                    R2Client.get().uploadObject(
                        selectedBucket.value!!.bucketName, taskState.task,
                        {
                            uploadingJobsMap[taskState.task.taskId] = UploadingJob(null, it)
                        }, { taskId, progress ->
                            println("uploading progress${progress}")
                            if (taskState.status.value == UploadTaskStatus.Pending) {
                                taskState.status.value = UploadTaskStatus.Progressing
                            }
                            taskState.process.value = progress

                        }, { taskId ->
                            println("finish")
                            taskState.process.value = 100.0
                            taskState.status.value = UploadTaskStatus.Finished
                        })
                }
                uploadingJobsMap[taskState.task.taskId]?.copy(deferred = job)

            }
        }
    }


    fun cancelUploadingTask(jobKey: String) {
        val uploadingJob = uploadingJobsMap[jobKey]
        uploadingJob?.deferred?.cancel()
        uploadingJob?.inputStream?.close()
    }

}
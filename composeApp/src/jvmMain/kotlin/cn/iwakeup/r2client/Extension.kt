package cn.iwakeup.r2client

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.routes.Route
import cn.iwakeup.r2client.ui.screens.upload.UploadTaskStatus
import cn.iwakeup.r2client.ui.screens.upload.UploadTaskUIState
import com.iwakeup.r2client.api.UploadTask
import software.amazon.awssdk.services.s3.model.Bucket
import java.io.File


fun <T> MutableState<T>.toImmutable(): State<T> {
    return this
}


fun File.toUploadTask(): UploadTask {
    return UploadTask(this.absolutePath, this)
}


fun UploadTask.stateOf(): UploadTaskUIState {
    return UploadTaskUIState(this, mutableStateOf(0.0), mutableStateOf(UploadTaskStatus.Pending))
}

fun Bucket.toBasicInfo(): BucketBasicInfo {
    return BucketBasicInfo(this.name(), this.creationDate().toString())
}

fun Route.Main.toAPIConfiguration(): APIConfiguration {
    return APIConfiguration(this.accountId, this.accessKey, this.secretKey)
}

fun APIConfiguration.toMainRoute(): Route.Main {
    return Route.Main(this.accountId, this.accessKey, this.secretKey)
}


fun Route.Splash.toAPIConfiguration(): APIConfiguration {
    return APIConfiguration(this.accountId, this.accessKey, this.secretKey)
}

fun APIConfiguration.toSplashRoute(freshLaunch: Boolean): Route.Splash {
    return Route.Splash(freshLaunch, this.accountId, this.accessKey, this.secretKey)
}
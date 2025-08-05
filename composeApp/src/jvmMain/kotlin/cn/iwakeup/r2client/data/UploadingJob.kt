package cn.iwakeup.r2client.data

import kotlinx.coroutines.Deferred
import java.io.InputStream

data class UploadingJob(val deferred: Deferred<Boolean>?, val inputStream: InputStream)

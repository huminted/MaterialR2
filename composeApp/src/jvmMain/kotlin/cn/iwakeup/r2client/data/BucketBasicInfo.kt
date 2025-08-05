package cn.iwakeup.r2client.data

import kotlinx.serialization.Serializable

@Serializable
data class BucketBasicInfo(val bucketName: String, val createTime: String)
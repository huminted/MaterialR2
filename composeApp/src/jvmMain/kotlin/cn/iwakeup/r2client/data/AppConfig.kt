package cn.iwakeup.r2client.data

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(val bucketList: List<BucketBasicInfo>, val apiConfiguration: APIConfiguration)

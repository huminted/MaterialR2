package cn.iwakeup.r2client.data

import software.amazon.awssdk.services.s3.model.Bucket

data class AppConfig(
    val bucketList: List<Bucket>,
    val apiConfiguration: APIConfiguration
)

package cn.iwakeup.r2client.data

import kotlinx.serialization.Serializable

@Serializable
data class APIConfiguration(
    val accountId: String,
    val accessKey: String,
    val secretKey: String
)

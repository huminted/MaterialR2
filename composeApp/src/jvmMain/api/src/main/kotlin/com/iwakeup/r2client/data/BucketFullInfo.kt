package com.iwakeup.r2client.data

import software.amazon.awssdk.services.s3.model.S3Object

data class BucketFullInfo(
    val index: Int,
    val bucketName: String,
    val createTime: String,
    val objects: List<S3Object>,
    val publicURL: String?
)



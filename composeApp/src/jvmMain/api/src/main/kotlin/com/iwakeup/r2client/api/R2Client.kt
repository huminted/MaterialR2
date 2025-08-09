package com.iwakeup.r2client.api

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.io.FileInputStream
import java.io.FilterInputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong


/**
 * Client for interacting with Cloudflare R2 Storage using AWS SDK S3 compatibility
 */
class R2Client(private val s3Client: S3Client) {
    /**
     * Lists all buckets in the R2 storage
     */
    fun listBuckets(): List<Bucket> {
        try {
            return s3Client.listBuckets().buckets()
        } catch (e: S3Exception) {
            throw RuntimeException("Failed to list buckets: " + e.message, e)
        }
    }

    /**
     * Lists all objects in the specified bucket
     */
    fun listObjects(bucketName: String): List<S3Object> {

        try {
            val objectList = mutableListOf<S3Object>()
            var continuationToken: String? = null

            do {
                val requestBuilder = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .maxKeys(1000)

                if (continuationToken != null) {
                    requestBuilder.continuationToken(continuationToken)
                }

                val response: ListObjectsV2Response = s3Client.listObjectsV2(requestBuilder.build())

                objectList.addAll(response.contents())

                continuationToken = if (response.isTruncated) response.nextContinuationToken() else null
            } while (false)


            return objectList
        } catch (e: S3Exception) {
            throw RuntimeException("Failed to list objects in bucket " + bucketName + ": " + e.message, e)
        }
    }


    fun uploadObject(
        bucketName: String,
        uploadTask: UploadTask,
        onStart: ((inputStream: InputStream) -> Unit)?,
        onProgress: (taskId: String, progress: Double) -> Unit,
        onFinished: (taskId: String) -> Unit
    ): Boolean {
        try {
            val file = uploadTask.file
            val request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.name)
                .contentType(file.extension)
                .build()

            val totalBytes = file.length()
            val fileInputStream = FileInputStream(file)
            val uploadedBytes = AtomicLong(0)

            val progressInputStream: InputStream = object : FilterInputStream(fileInputStream) {

                override fun read(b: ByteArray, off: Int, len: Int): Int {
                    val read = super.read(b, off, len)
                    if (read > 0) {
                        val uploaded: Long = uploadedBytes.addAndGet(read.toLong())
                        val percent: Double = ((100.0 * uploaded / totalBytes) * 100).toInt() / 100.0
                        if (percent == 100.0) {
                            onFinished(uploadTask.taskId)
                        } else {
                            onProgress(uploadTask.taskId, percent)
                        }
                    }
                    return read
                }
            }
            onStart?.invoke(progressInputStream)
            s3Client.putObject(request, RequestBody.fromInputStream(progressInputStream, totalBytes))


            return true
        } catch (e: S3Exception) {
            throw RuntimeException("Failed to upload object in bucket " + bucketName + ": " + e.message, e)
        }

    }


    fun searchObject(bucket: String, key: String): List<S3Object> {
        val objectList = mutableListOf<S3Object>()
        var continuationToken: String? = null

        do {
            val requestBuilder = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(key)
                .maxKeys(1000)

            if (continuationToken != null) {
                requestBuilder.continuationToken(continuationToken)
            }

            val response: ListObjectsV2Response = s3Client.listObjectsV2(requestBuilder.build())

            objectList.addAll(response.contents())

            continuationToken = if (response.isTruncated) response.nextContinuationToken() else null
        } while (continuationToken != null)

        return objectList
    }

    companion object Companion {
        private var instance: R2Client? = null

        fun init(accountId: String, accessKey: String, secretKey: String): List<Bucket> {
            val s3Config = S3ClientBuilder.S3Config(
                accountId,
                accessKey,
                secretKey
            )
            try {
                val s3Client = S3ClientBuilder.buildS3Client(s3Config)
                instance = R2Client(s3Client)
                return s3Client.listBuckets().buckets()
            } catch (e: Exception) {
                throw e
            }


        }

        fun get(): R2Client {
            if (instance == null) {
                throw IllegalStateException("call init first")
            }
            return instance!!
        }
    }
}

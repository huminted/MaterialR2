package com.iwakeup.r2client.api

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import java.net.URI

object S3ClientBuilder {

    class S3Config(accountId: String, val accessKey: String, val secretKey: String) {
        val endpoint: String = String.format("https://%s.r2.cloudflarestorage.com", accountId)
    }

     fun buildS3Client(config: S3Config): S3Client {
        val credentials = AwsBasicCredentials.create(
            config.accessKey,
            config.secretKey
        )

        val serviceConfiguration = S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .build()

        return S3Client.builder()
            .endpointOverride(URI.create(config.endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("auto"))
            .serviceConfiguration(serviceConfiguration)
            .build()
    }

}
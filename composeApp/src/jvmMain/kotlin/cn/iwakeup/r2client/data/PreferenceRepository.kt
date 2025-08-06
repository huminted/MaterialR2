package cn.iwakeup.r2client.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.getString
import software.amazon.awssdk.services.s3.model.Bucket

class PreferenceRepository(val settingDataStore: DataStore<Preferences>) {

    val accountIdKey = stringPreferencesKey("r2_api_account_key")
    val accessKey = stringPreferencesKey("r2_api_access_key")
    val secretKey = stringPreferencesKey("r2_api_secret_key")

    private val bucketPublicURLSuffix = "_pub_url_key"

    private fun buildBucketPublicURLKey(bucketName: String): String {
        return bucketName + bucketPublicURLSuffix
    }

    suspend fun saveBucketPublicURL(bucketNameAndURL: List<Pair<String, String>>) {
        settingDataStore.updateData { pre ->
            pre.toMutablePreferences().apply {

                bucketNameAndURL.forEach {
                    set(stringPreferencesKey(buildBucketPublicURLKey(it.first)), it.second)
                }
            }
        }
    }

    suspend fun getBucketPublicURL(bucketName: String): Flow<String?> {
        return flow {
            settingDataStore.data.collect {
                val value = it[stringPreferencesKey(buildBucketPublicURLKey(bucketName))]
                emit(value)
            }
        }
    }

    suspend fun getBucketsPublicURL(buckets: List<Bucket>): Flow<List<String?>> {
        return flow {
            settingDataStore.data.collect {
                val result = buckets.map { bucket ->
                    it[stringPreferencesKey(buildBucketPublicURLKey(bucket.name()))]
                }
                emit(result)
            }
        }
    }

    suspend fun saveAPIConfiguration(apiConfiguration: APIConfiguration) {
        settingDataStore.updateData { pre ->
            pre.toMutablePreferences().apply {
                set(accountIdKey, apiConfiguration.accountId)
                set(accessKey, apiConfiguration.accessKey)
                set(secretKey, apiConfiguration.secretKey)
            }
        }
    }

    fun getAPIConfiguration(): Flow<APIConfiguration?> {
        return flow {
            settingDataStore.data.collect {
                if (it.contains(accountIdKey) && it.contains(accountIdKey) && it.contains(secretKey)) {
                    emit(
                        APIConfiguration(
                            it[accountIdKey]!!,
                            it[accessKey]!!,
                            it[secretKey]!!,
                        )
                    )
                } else {
                    emit(null)
                }


            }
        }
    }
}

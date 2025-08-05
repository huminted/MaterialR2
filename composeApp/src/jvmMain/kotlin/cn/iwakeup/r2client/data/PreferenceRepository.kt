package cn.iwakeup.r2client.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PreferenceRepository(val settingDataStore: DataStore<Preferences>) {

    val accountIdKey = stringPreferencesKey("r2_api_account_key")
    val accessKey = stringPreferencesKey("r2_api_access_key")
    val secretKey = stringPreferencesKey("r2_api_secret_key")

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

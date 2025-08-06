package cn.iwakeup.r2client.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.AppConfig
import cn.iwakeup.r2client.data.PreferenceRepository
import cn.iwakeup.r2client.data.PreferencesDataStore
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class SplashUIState {
    object Initiating : SplashUIState()
    object FreshInstall : SplashUIState()
    data class InitiatedFail(val reason: String) : SplashUIState()
    data class InitiatedSuccess(val appConfig: AppConfig, val needSetupBucketPublicURL: Boolean) : SplashUIState()
}


class SplashViewModel : ViewModel() {

    private val _splashUIState = MutableStateFlow<SplashUIState>(SplashUIState.Initiating)
    val splashUIState: StateFlow<SplashUIState> = _splashUIState

    private var _setupBucketPublicURL = mutableStateOf(false)


    private val repository = PreferenceRepository(PreferencesDataStore.instance)


    fun refreshR2ClientConfiguration(configuration: APIConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            _splashUIState.emit(SplashUIState.Initiating)
            initR2ClientInternal(configuration)
        }
    }


    fun initR2ClientForFreshLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            _splashUIState.emit(SplashUIState.Initiating)
            repository.getAPIConfiguration()
                .collect { configuration ->
                    if (configuration == null) {
                        _splashUIState.emit(SplashUIState.FreshInstall)
                        _setupBucketPublicURL.value = true
                    } else {
                        initR2ClientInternal(configuration)
                    }
                }
        }

    }

    fun saveAPIConfiguration(apiConfiguration: APIConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            _setupBucketPublicURL.value = true
            repository.saveAPIConfiguration(apiConfiguration)
            refreshR2ClientConfiguration(apiConfiguration)
        }


    }

    fun initR2ClientInternal(configuration: APIConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val buckets = R2Client.init(configuration.accountId, configuration.accessKey, configuration.secretKey)
                _splashUIState.emit(
                    SplashUIState.InitiatedSuccess(
                        AppConfig(
                            buckets,
                            configuration
                        ),
                        _setupBucketPublicURL.value
                    )
                )


            } catch (e: Exception) {
                _splashUIState.emit(SplashUIState.InitiatedFail(e.message.toString()))
            }

        }
    }

    fun saveBucketPublicURL(bucketNameAndURL: List<Pair<String, String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveBucketPublicURL(bucketNameAndURL)
        }
    }


}
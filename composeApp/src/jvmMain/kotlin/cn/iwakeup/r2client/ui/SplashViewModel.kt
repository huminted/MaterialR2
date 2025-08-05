package cn.iwakeup.r2client.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.AppConfig
import cn.iwakeup.r2client.data.PreferenceRepository
import cn.iwakeup.r2client.data.PreferencesDataStore
import cn.iwakeup.r2client.toBasicInfo
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed interface SplashUIState {
    object Initiating : SplashUIState
    object FreshInstall : SplashUIState
    data class InitiatedFail(val reason: String) : SplashUIState
    data class InitiatedSuccess(val appConfig: AppConfig) : SplashUIState
}


class SplashViewModel : ViewModel() {

    private val _splashUIState = MutableStateFlow<SplashUIState>(SplashUIState.Initiating)
    val splashUIState: StateFlow<SplashUIState> = _splashUIState


    private val repository = PreferenceRepository(PreferencesDataStore.instance)


    fun initR2Client(configuration: APIConfiguration) {
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
                    } else {
                        initR2ClientInternal(configuration)
                    }
                }
        }

    }

    fun saveAPIConfiguration(apiConfiguration: APIConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAPIConfiguration(apiConfiguration)
            initR2Client(apiConfiguration)
        }


    }

    suspend fun initR2ClientInternal(configuration: APIConfiguration) {


        try {
            val buckets = R2Client.init(
                configuration.accountId, configuration.accessKey, configuration.secretKey
            ).map { it.toBasicInfo() }
            _splashUIState.emit(SplashUIState.InitiatedSuccess(AppConfig(buckets, configuration)))

        } catch (e: Exception) {
            _splashUIState.emit(SplashUIState.InitiatedFail(e.message.toString()))
        }
    }


}
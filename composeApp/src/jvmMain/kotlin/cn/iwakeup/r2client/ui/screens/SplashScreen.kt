package cn.iwakeup.r2client.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.AppConfig
import cn.iwakeup.r2client.ui.SplashUIState
import cn.iwakeup.r2client.ui.SplashViewModel
import cn.iwakeup.r2client.ui.components.InitialLoadingIndicator
import cn.iwakeup.r2client.ui.screens.setting.R2APIConfigurationForm


@Composable
fun SplashScreen(
    freshLaunch: Boolean,
    apiConfiguration: APIConfiguration, onInitiatedSuccess: (AppConfig) -> Unit
) {
    val splashViewModel = remember { SplashViewModel() }

    val accountIdState = rememberTextFieldState(initialText = apiConfiguration.accountId)
    val accessKeyState = rememberTextFieldState(initialText = apiConfiguration.accessKey)
    val secretKeyState = rememberTextFieldState(initialText = apiConfiguration.secretKey)

    val appUIState by splashViewModel.splashUIState.collectAsStateWithLifecycle()


    LaunchedEffect(freshLaunch) {
        if (freshLaunch) {
            splashViewModel.initR2ClientForFreshLaunch()
        } else {
            splashViewModel.initR2Client(apiConfiguration)
        }
    }



    when (appUIState) {
        SplashUIState.Initiating -> {
            InitialLoadingIndicator(Modifier.fillMaxSize(), "初始化中")
        }

        SplashUIState.FreshInstall -> {
            Column {
                Text(modifier = Modifier.padding(15.dp), text = "R2 API 初始化设置")
                ConfigurationForm(accountIdState, accessKeyState, secretKeyState) {
                    splashViewModel.saveAPIConfiguration(it)
                }
            }

        }

        is SplashUIState.InitiatedFail -> {
            val railReason = appUIState as SplashUIState.InitiatedFail
            Column {
                Text(modifier = Modifier.padding(15.dp), text = "R2 API 初始化失败:${railReason.reason}")
                ConfigurationForm(accountIdState, accessKeyState, secretKeyState) {
                    splashViewModel.saveAPIConfiguration(it)
                }
            }
        }

        is SplashUIState.InitiatedSuccess -> {
            val successState = appUIState as SplashUIState.InitiatedSuccess
            onInitiatedSuccess(successState.appConfig)
        }

    }


}

@Composable
private fun ConfigurationForm(
    accountIdState: TextFieldState,
    accessKeyState: TextFieldState,
    secretKeyState: TextFieldState,
    onSave: (APIConfiguration) -> Unit
) {
    Box(Modifier.padding(15.dp)) {
        R2APIConfigurationForm(accountIdState, accessKeyState, secretKeyState, onSave)
    }
}
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.AppConfig
import cn.iwakeup.r2client.isSame
import cn.iwakeup.r2client.ui.SplashUIState
import cn.iwakeup.r2client.ui.SplashViewModel
import cn.iwakeup.r2client.ui.components.InitialLoadingIndicator
import cn.iwakeup.r2client.ui.components.form.BucketConfigurationInitialForm
import cn.iwakeup.r2client.ui.components.form.R2APIConfigurationForm


@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    freshInitialization: Boolean,
    unCheckedApiConfiguration: APIConfiguration, onInitiatedSuccess: (AppConfig) -> Unit
) {


    val appUIState by splashViewModel.splashUIState.collectAsStateWithLifecycle()


    LaunchedEffect(freshInitialization) {
        if (freshInitialization) {
            splashViewModel.initR2ClientForFreshLaunch()
        } else {
            splashViewModel.refreshR2ClientConfiguration(unCheckedApiConfiguration)
        }
    }



    when (appUIState) {
        SplashUIState.Initiating -> {
            InitialLoadingIndicator(Modifier.fillMaxSize(), "Initializing")
        }

        SplashUIState.FreshInstall -> {
            Column {
                Text(modifier = Modifier.padding(15.dp), text = "R2 API Initial Setup")
                APIConfigurationForm(
                    rememberTextFieldState(),
                    rememberTextFieldState(),
                    rememberTextFieldState(),
                    { true }) {
                    splashViewModel.saveAPIConfiguration(it)
                }
            }

        }

        is SplashUIState.InitiatedFail -> {
            val frailReason = appUIState as SplashUIState.InitiatedFail
            val failAPIConfiguration = frailReason.apiConfiguration
            Column {
                val accountIdState = rememberTextFieldState(initialText = failAPIConfiguration.accountId)
                val accessKeyState = rememberTextFieldState(initialText = failAPIConfiguration.accessKey)
                val secretKeyState = rememberTextFieldState(initialText = failAPIConfiguration.secretKey)
                Text(modifier = Modifier.padding(15.dp), text = "R2 API initialization failed:${frailReason.reason}")
                APIConfigurationForm(accountIdState, accessKeyState, secretKeyState, {
                    failAPIConfiguration.isSame(
                        accountIdState.text.toString(),
                        accessKeyState.text.toString(),
                        secretKeyState.text.toString()
                    )
                }) {
                    splashViewModel.saveAPIConfiguration(it)
                }
            }
        }

        is SplashUIState.InitiatedSuccess -> {
            val successState = appUIState as SplashUIState.InitiatedSuccess
            if (successState.needSetupBucketPublicURL || !freshInitialization) {
                BucketConfigurationInitialForm(Modifier.padding(20.dp), successState.appConfig.bucketList) {
                    splashViewModel.saveBucketPublicURL(it)
                    onInitiatedSuccess(successState.appConfig)
                }
            } else {
                onInitiatedSuccess(successState.appConfig)
            }
        }

    }


}

@Composable
private fun APIConfigurationForm(
    accountIdState: TextFieldState,
    accessKeyState: TextFieldState,
    secretKeyState: TextFieldState,
    saveButtonEnabled: () -> Boolean,
    onSave: (APIConfiguration) -> Unit
) {
    Box(Modifier.padding(15.dp)) {
        R2APIConfigurationForm(accountIdState, accessKeyState, secretKeyState, saveButtonEnabled, onSave)
    }
}
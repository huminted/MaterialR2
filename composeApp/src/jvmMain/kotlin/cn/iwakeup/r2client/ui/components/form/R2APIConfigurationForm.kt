package cn.iwakeup.r2client.ui.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.isSame


@Composable
fun R2APIConfigurationForm(
    modifier: Modifier = Modifier,
    apiConfiguration: APIConfiguration,
    onSave: (APIConfiguration) -> Unit
) {
    val accountIdState = rememberTextFieldState(initialText = apiConfiguration.accountId)
    val accessKeyState = rememberTextFieldState(initialText = apiConfiguration.accessKey)
    val secretKeyState = rememberTextFieldState(initialText = apiConfiguration.secretKey)



    Column(modifier) {
        R2APIConfigurationForm(
            accountIdState,
            accessKeyState,
            secretKeyState, {
                apiConfiguration.isSame(
                    accountIdState.text.toString(),
                    accessKeyState.text.toString(),
                    secretKeyState.text.toString()
                )
            },
            onSave
        )
    }
}


@Composable
fun R2APIConfigurationForm(
    accountIdState: TextFieldState,
    accessKeyState: TextFieldState,
    secretKeyState: TextFieldState,
    saveButtonEnabled: () -> Boolean,
    onSave: (APIConfiguration) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("R2 API 设置")
        AppThemeOutlinedTextField(
            modifier = Modifier,
            state = accountIdState,
            label = "AccountId"
        )
        AppThemeOutlinedTextField(
            modifier = Modifier,
            state = accessKeyState,
            label = "AccessKey"
        )
        AppThemeOutlinedTextField(
            modifier = Modifier,
            state = secretKeyState,
            label = "SecretKey"
        )
        OutlinedButton(onClick = {
            onSave(
                APIConfiguration(
                    accountIdState.text.toString(),
                    accessKeyState.text.toString(),
                    secretKeyState.text.toString()
                )
            )
        }, enabled = saveButtonEnabled()) {
            Text("Save")
        }
    }
}
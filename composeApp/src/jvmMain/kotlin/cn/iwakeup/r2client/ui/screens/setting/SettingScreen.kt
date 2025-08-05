package cn.iwakeup.r2client.ui.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.APIConfiguration

@Composable
fun SettingScreen(
    apiConfiguration: APIConfiguration,
    onSave: (apiConfiguration: APIConfiguration) -> Unit
) {


    val accountIdState = rememberTextFieldState(initialText = apiConfiguration.accountId)
    val accessKeyState = rememberTextFieldState(initialText = apiConfiguration.accessKey)
    val secretKeyState = rememberTextFieldState(initialText = apiConfiguration.secretKey)

    Column(Modifier.padding(10.dp)) {
        R2APIConfigurationForm(
            accountIdState,
            accessKeyState,
            secretKeyState,
            onSave
        )

    }


}


@Composable
fun R2APIConfigurationForm(
    accountIdState: TextFieldState,
    accessKeyState: TextFieldState,
    secretKeyState: TextFieldState,
    onSave: (APIConfiguration) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("R2 API 设置")
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = accountIdState,
            label = { Text("AccountId") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = accessKeyState,
            label = { Text("AccessKey") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = secretKeyState,
            label = { Text("SecretKey") }
        )
        OutlinedButton(onClick = {
            onSave(
                APIConfiguration(
                    accountIdState.text.toString(),
                    accessKeyState.text.toString(),
                    secretKeyState.text.toString()
                )
            )
        }) {
            Text("Save")

        }
    }
}
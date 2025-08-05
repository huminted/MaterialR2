package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.ui.screens.setting.SettingScreen

@Composable
fun SettingRoute(apiConfiguration: APIConfiguration, onSave: (apiConfiguration: APIConfiguration) -> Unit) {


    SettingScreen(apiConfiguration, onSave)
}
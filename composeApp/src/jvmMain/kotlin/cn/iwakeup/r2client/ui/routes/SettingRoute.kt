package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.setting.SettingScreen

@Composable
fun SettingRoute(
    apiConfiguration: APIConfiguration,
    buckets: List<BucketBasicInfo>,
    onSave: (apiConfiguration: APIConfiguration) -> Unit
) {

    val viewModel =

        SettingScreen(apiConfiguration, buckets, onSave)
}
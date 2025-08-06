package cn.iwakeup.r2client.ui.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.components.form.BucketConfigurationForm
import cn.iwakeup.r2client.ui.components.form.R2APIConfigurationForm

@Composable
fun SettingScreen(
    apiConfiguration: APIConfiguration,
    buckets: List<BucketBasicInfo>,
    onSaveAPIConfigurationSuccess: (apiConfiguration: APIConfiguration) -> Unit
) {
    val viewModel = remember { SettingViewModel() }

    Row(
        Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
    ) {

        Surface(shape = RoundedCornerShape(10.dp)) {
            R2APIConfigurationForm(
                Modifier.padding(20.dp).wrapContentSize(),
                apiConfiguration = apiConfiguration
            ) {
                viewModel.saveAPIConfiguration(it)
                onSaveAPIConfigurationSuccess(it)
            }
        }

        Surface(shape = RoundedCornerShape(10.dp)) {
            BucketConfigurationForm(Modifier.padding(20.dp), bucketList = buckets) {
                viewModel.saveBucketPublicURL(it)
            }
        }
    }
}

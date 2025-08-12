package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.upload.UploadScreen
import cn.iwakeup.r2client.ui.screens.upload.UploadViewModel

@Composable
fun UploadRoute(navBackStackEntry: NavBackStackEntry, bucketBasicInfoList: List<BucketBasicInfo>) {
    val uploadViewModel: UploadViewModel =
        viewModel(
            viewModelStoreOwner = navBackStackEntry,
            factory = UploadViewModel.Factory
        )
    UploadScreen(bucketBasicInfoList, uploadViewModel)
}
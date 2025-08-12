package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.bucket.BucketScreen
import cn.iwakeup.r2client.ui.screens.bucket.BucketViewModel


@Composable
fun BucketRoute(navBackStackEntry: NavBackStackEntry, bucketBasicInfoList: List<BucketBasicInfo>) {
    val bucketViewModel: BucketViewModel =
        viewModel(viewModelStoreOwner = navBackStackEntry, factory = BucketViewModel.Factory)

    println(bucketViewModel)

    BucketScreen(bucketBasicInfoList, bucketViewModel)
}
package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.bucket.BucketScreen
import cn.iwakeup.r2client.ui.screens.bucket.BucketViewModel


@Composable
fun BucketRoute(bucketBasicInfoList: List<BucketBasicInfo>, bucketViewModel: BucketViewModel) {
    BucketScreen(bucketBasicInfoList, bucketViewModel)
}
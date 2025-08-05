package cn.iwakeup.r2client.ui.routes

import androidx.compose.runtime.Composable
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.upload.UploadScreen
import cn.iwakeup.r2client.ui.screens.upload.UploadViewModel

@Composable
fun UploadRoute(bucketBasicInfoList: List<BucketBasicInfo>, uploadViewModel: UploadViewModel) {


    UploadScreen(bucketBasicInfoList, uploadViewModel)
}
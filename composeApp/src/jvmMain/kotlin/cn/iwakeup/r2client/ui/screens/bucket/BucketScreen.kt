package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.data.BucketBasicInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun BucketScreen(bucketBasicInfoList: List<BucketBasicInfo>, bucketViewModel: BucketViewModel) {


    val uiState by bucketViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(bucketBasicInfoList) {
        withContext(Dispatchers.IO) {
            bucketViewModel.getBucketsInfo(bucketBasicInfoList)
        }
    }


    Column {
        LazyVerticalGrid(
            GridCells.Adaptive(200.dp),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), content = {
                items(uiState.bucketFullInfoList.size, key = { uiState.bucketFullInfoList[it].bucketName }) {
                    BucketCardItem(uiState.bucketFullInfoList[it]) {
                        //
                    }
                }
            })

        if (uiState.isLoading) {
            Text("加载其他中")
        }
    }


}
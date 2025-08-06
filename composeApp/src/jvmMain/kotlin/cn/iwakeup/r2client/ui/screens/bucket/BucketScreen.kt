package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.theme.AppTheme
import com.iwakeup.r2client.data.BucketFullInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun BucketScreen(bucketBasicInfoList: List<BucketBasicInfo>, bucketViewModel: BucketViewModel) {
    var isExpanded by remember { mutableStateOf(false) }

    val sideListWeight by animateFloatAsState(
        targetValue = if (isExpanded) 0.2f else 0.1f,
        animationSpec = tween(durationMillis = 200),
        label = "rail_width"
    )

    val uiState by bucketViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(bucketBasicInfoList) {
        isExpanded = true
        withContext(Dispatchers.IO) {
            bucketViewModel.getBucketsInfo(bucketBasicInfoList)
        }
    }

    var selectedBucket by remember { mutableStateOf<BucketFullInfo?>(null) }
    Row {
        BucketsSideList(
            Modifier.weight(sideListWeight).background(AppTheme.colors.surfaceContainerLow),
            uiState.bucketFullInfoList
        ) {
            selectedBucket = it
        }
        Box(Modifier.weight(0.8f).fillMaxHeight().padding(10.dp)) {
            if (selectedBucket == null) {
                Text("点击左侧选择")
            } else {
                Text(selectedBucket!!.bucketName)
            }
        }
    }


}
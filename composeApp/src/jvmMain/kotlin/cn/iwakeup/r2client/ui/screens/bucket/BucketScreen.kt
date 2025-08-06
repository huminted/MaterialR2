package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.resource.Res
import cn.iwakeup.r2client.resource.test

import cn.iwakeup.r2client.ui.theme.AppTheme
import com.iwakeup.r2client.data.BucketFullInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import software.amazon.awssdk.services.s3.model.S3Object


@Composable
fun BucketScreen(bucketBasicInfoList: List<BucketBasicInfo>, bucketViewModel: BucketViewModel) {
    var isExpanded by remember { mutableStateOf(false) }

    val sideListWeight by animateFloatAsState(
        targetValue = if (isExpanded) 0.16f else 0.001f,
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

        Box(
            Modifier.weight(0.8f).fillMaxHeight().background(AppTheme.colors.surfaceContainerLow),
        ) {
            if (selectedBucket == null) {
                Text("共${bucketBasicInfoList.size}个 Buckets")
                Image(
                    painter = painterResource(Res.drawable.test),
                    contentDescription = "描述文字",
                    modifier = Modifier.size(200.dp)
                )

            } else {
                val curBucket = selectedBucket!!

                BucketObjectList(
                    curBucket,
                    onCopyLink = {
                        bucketViewModel.copyObjectLink(curBucket, it)
                    },
                    onDeleteObject = {
                        bucketViewModel.deleteObject(curBucket, it)
                    })
            }
        }
    }


}


@Composable
private fun BucketObjectList(
    bucketBasicInfo: BucketFullInfo,
    onCopyLink: (String) -> Unit,
    onDeleteObject: (String) -> Unit,
) {

    val objects = bucketBasicInfo.objects
    LazyColumn(Modifier.fillMaxWidth()) {

        stickyHeader {
            BucketObjectListHeader(bucketBasicInfo)
        }

        items(objects.size, key = { objects[it] }) {
            val obj = objects[it]
            ObjectItem(obj, !bucketBasicInfo.publicURL.isNullOrEmpty(), onCopyLink, onDeleteObject)
        }
    }
}

@Composable
fun BucketObjectListHeader(bucketBasicInfo: BucketFullInfo) {
    Column(
        Modifier.background(AppTheme.colors.surfaceContainerLow).fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = "Object List", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        if (bucketBasicInfo.publicURL.isNullOrEmpty()) {
            Text("未配置Public URL,无法拷贝链接", color = AppTheme.colors.error, fontSize = 11.sp)
        } else {
            Text(bucketBasicInfo.publicURL.toString(), color = AppTheme.colors.onSecondaryContainer, fontSize = 11.sp)
        }
    }
}


@Composable
private fun ObjectItem(
    s3Object: S3Object,
    enableCopyLink: Boolean,
    onCopyLink: (String) -> Unit,
    onDeleteObject: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "")
        Column(Modifier.weight(1f)) {
            Text(s3Object.key())
            Text(text = s3Object.lastModified().toString(), fontSize = 11.sp, color = AppTheme.colors.outline)
        }
        IconButton(onClick = { onCopyLink(s3Object.key()) }, enabled = enableCopyLink) {
            Icon(
                modifier = Modifier.align(Alignment.Top),
                imageVector = Icons.Default.Link,
                contentDescription = "Link"
            )
        }
        IconButton(onClick = { onDeleteObject(s3Object.key()) }) {
            Icon(
                modifier = Modifier.align(Alignment.Top),
                imageVector = Icons.Default.Delete,
                contentDescription = "Link"
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp)
}





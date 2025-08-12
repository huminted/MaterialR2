package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.iwakeup.r2client.APP_NAME
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.theme.AppTheme
import com.iwakeup.r2client.data.BucketFullInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.services.s3.model.S3Object


@Composable
fun BucketScreen(bucketBasicInfoList: List<BucketBasicInfo>, bucketViewModel: BucketViewModel) {

    var selectedBucket by remember { mutableStateOf<BucketFullInfo?>(null) }

    val offsetX = remember { Animatable(-200f) }

    LaunchedEffect(Unit) {
        offsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 200)
        )
    }

    val uiState by bucketViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(bucketBasicInfoList) {
        withContext(Dispatchers.IO) {
            bucketViewModel.getBucketsInfo(bucketBasicInfoList)
        }
    }

    Row {
        BucketsSideList(Modifier.weight(0.2f).offset { IntOffset(offsetX.value.toInt(), 0) }
            .background(AppTheme.colors.surfaceContainerLow),
            uiState.bucketFullInfoList
        ) {
            selectedBucket = it
        }

        Box(
            Modifier.weight(0.8f).fillMaxHeight().background(AppTheme.colors.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            if (selectedBucket == null) {
                Text(
                    text = APP_NAME,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.secondaryContainer
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
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {

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

    Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.background(AppTheme.colors.surfaceContainerLow).padding(vertical = 20.dp, horizontal = 20.dp)
                .fillMaxWidth(),

            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = "Object List", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

            if (bucketBasicInfo.publicURL.isNullOrEmpty()) {
                Text(
                    "Public URL is not configured, the link cannot be copied.",
                    color = AppTheme.colors.error,
                    fontSize = 11.sp
                )
            } else {
                Text(
                    bucketBasicInfo.publicURL.toString(),
                    color = AppTheme.colors.onSecondaryContainer,
                    fontSize = 11.sp
                )
            }
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

    Surface(
        Modifier.padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "",
                tint = AppTheme.colors.primary
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s3Object.key())
                Text(text = s3Object.lastModified().toString(), fontSize = 11.sp, color = AppTheme.colors.outline)
            }
            IconButton(onClick = { onCopyLink(s3Object.key()) }, enabled = enableCopyLink) {
                Icon(
                    modifier = Modifier.align(Alignment.Top).size(20.dp),
                    imageVector = Icons.Default.Link,
                    contentDescription = "Link"
                )
            }
            IconButton(onClick = { onDeleteObject(s3Object.key()) }) {
                Icon(
                    modifier = Modifier.align(Alignment.Top).size(20.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Link"
                )
            }
        }
    }
}





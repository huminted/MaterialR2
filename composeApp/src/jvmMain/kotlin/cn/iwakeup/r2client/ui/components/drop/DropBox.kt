@file:OptIn(ExperimentalComposeUiApi::class)

package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.theme.AppTheme
import java.io.File


@Composable
fun DropComponent(
    file: File?, onDropFile: (droppedFile: File) -> Unit,
    buckets: List<BucketBasicInfo>,
    selectedBucket: BucketBasicInfo,
    onSelectedChange: (selectedBucket: BucketBasicInfo) -> Unit,
    fileList: @Composable () -> Unit
) {

    Column {
        DropBox(file, { file ->
            onDropFile(file)
        }, buckets, selectedBucket, onSelectedChange)
        file?.let {
            Spacer(Modifier.height(10.dp))
            fileList()
        }
    }
}

@Composable
private fun DropBox(
    droppedFile: File?,
    onDropFile: (droppedFile: File) -> Unit,
    buckets: List<BucketBasicInfo>,
    selectedBucket: BucketBasicInfo,
    onSelectedChange: (selectedBucket: BucketBasicInfo) -> Unit
) {
    val fileDropTarget = remember {
        FileDropTarget(
            onDropFile = onDropFile,
            onDropDirectory = onDropFile
        )
    }
    Column(
        modifier = Modifier
            .background(AppTheme.colors.secondaryContainer, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(24.dp)
            .dragAndDropTarget({ true }, fileDropTarget),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
    ) {
        Text("文件拖拽到此处 并选择Bucket", color = AppTheme.colors.onSecondaryContainer)
        BucketDropdownMenu(buckets, selectedBucket, onSelectedChange)
        droppedFile?.let { Text(it.absolutePath, color = AppTheme.colors.onSecondaryContainer) }
    }

}



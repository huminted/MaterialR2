@file:OptIn(ExperimentalComposeUiApi::class)

package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Outbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.theme.AppTheme
import java.io.File


@Composable
fun DropComponent(
    file: State<File?>, onDropFile: (droppedFile: File) -> Unit,
    buckets: List<BucketBasicInfo>,
    selectedBucket: BucketBasicInfo,
    onSelectedChange: (selectedBucket: BucketBasicInfo) -> Unit,
    fileList: @Composable () -> Unit
) {
    Column {
        DropBox({ file ->
            onDropFile(file)
        }, buckets, selectedBucket, onSelectedChange)
        file.value?.let {
            Spacer(Modifier.height(10.dp))
            fileList()
        }
    }
}

@Composable
fun DropBox(
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
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {

        Icon(imageVector = Icons.Rounded.Outbox, contentDescription = "", Modifier.size(40.dp))
        Text(
            "Drag and drop a file or folder to upload",
            color = AppTheme.colors.onSecondaryContainer,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.alpha(0.5f),
            text = "and, choose a bucket using dropdown menu below",
            color = AppTheme.colors.onSecondaryFixed,
        )
        Spacer(Modifier.height(10.dp))
        BucketDropdownMenu(buckets, selectedBucket, onSelectedChange)
    }

}



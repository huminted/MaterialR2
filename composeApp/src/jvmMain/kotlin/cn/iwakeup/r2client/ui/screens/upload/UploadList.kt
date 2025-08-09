package cn.iwakeup.r2client.ui.screens.upload

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.iwakeup.r2client.ui.components.DownloadProgressIndicator
import cn.iwakeup.r2client.ui.theme.AppTheme
import java.io.File


@Composable
fun UploadList(
    bucketHasPublicURL: () -> Boolean,
    fileList: List<UploadTaskUIState>, onRemoveItem: ((File) -> Unit)? = null,
    onCopyLink: ((File) -> Unit)? = null
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        items(fileList.size, {
            fileList[it].hashCode()
        }) {
            val uploadTask = fileList[it]
            val enableCopy = uploadTask.status.value == UploadTaskStatus.Finished && bucketHasPublicURL()
            UploadFileItem(
                enableCopy,
                uploadTask,
                onCopyLink = onCopyLink,
                onRemoveItem = onRemoveItem
            )
        }
    }
}


@Composable
fun UploadFileItem(
    enableCopy: Boolean,
    uploadTask: UploadTaskUIState,
    onRemoveItem: ((File) -> Unit)? = null,
    onCopyLink: ((File) -> Unit)? = null
) {
    val file = uploadTask.task.file


    Row(Modifier.padding(vertical = 4.dp)) {
        Column(Modifier.weight(0.9f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(fontWeight = FontWeight.SemiBold, text = file.name)
                if (uploadTask.status.value == UploadTaskStatus.Finished) {
                    Icon(
                        Icons.Default.CheckCircle,
                        "done",
                        Modifier.size(15.dp),
                        tint = AppTheme.colors.onSecondaryContainer
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(file.absolutePath, fontSize = 11.sp)
            DownloadProgressIndicator(uploadTask.process.value, uploadTask.status.value)

        }
        IconButton(
            modifier = Modifier.weight(0.05f),
            enabled = enableCopy,
            onClick = {
                onCopyLink?.invoke(file)
            },
            content = {
                Icon(Icons.Default.Link, "Link")
            })
        IconButton(modifier = Modifier.weight(0.05f), onClick = { onRemoveItem?.invoke(file) }, content = {
            Icon(Icons.Default.Close, "Close")
        })
    }
    Divider()


}
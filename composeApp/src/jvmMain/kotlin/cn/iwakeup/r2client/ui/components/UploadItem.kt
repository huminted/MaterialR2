package cn.iwakeup.r2client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.iwakeup.r2client.ui.screens.upload.UploadTaskStatus
import cn.iwakeup.r2client.ui.screens.upload.UploadTaskUIState
import java.io.File


@Composable
fun UploadFileItem(
    uploadTask: UploadTaskUIState,
    onRemoveItem: ((file: File) -> Unit)? = null,
    onCopyLink: ((file: File) -> Unit)? = null
) {
    val file = uploadTask.task.file

    Row(Modifier.padding(vertical = 4.dp)) {
        Column(Modifier.weight(0.9f)) {
            Text(fontWeight = FontWeight.SemiBold, text = file.name)
            Spacer(Modifier.height(2.dp))
            Text(file.absolutePath, fontSize = 11.sp)
            DownloadProgressIndicator(uploadTask.process.value, uploadTask.status.value)

        }
        IconButton(
            modifier = Modifier.weight(0.05f),
            enabled = uploadTask.status.value == UploadTaskStatus.Finished,
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

@Composable
fun DownloadProgressIndicator(progress: Double, status: UploadTaskStatus) {
    Column {
        Spacer(Modifier.height(8.dp))
        when (status) {
            UploadTaskStatus.Pending -> {
                Text(text = "Waiting", fontSize = 11.sp, color = Color.Gray)
            }

            UploadTaskStatus.Progressing -> {
                Spacer(Modifier.height(1.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { (progress / 100).toFloat() },
                )
                Spacer(Modifier.height(2.dp))
                Text(text = "${progress}%", fontSize = 11.sp, color = Color.Gray)

            }

            UploadTaskStatus.Finished -> {
                Text(text = "Done", fontSize = 11.sp, color = Color.Gray)
            }
        }

    }
}
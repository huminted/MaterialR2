@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package cn.iwakeup.r2client.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.iwakeup.r2client.ui.screens.upload.UploadTaskStatus

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
                LinearWavyProgressIndicator(
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
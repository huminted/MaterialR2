@file:OptIn(ExperimentalTime::class)

package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iwakeup.r2client.data.BucketFullInfo
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@Preview
@Composable
fun BucketCardItem(bucket: BucketFullInfo, onClick: () -> Unit) {

    OutlinedCard(onClick = onClick) {
        Column(Modifier.padding(10.dp)) {
            Text(
                text = bucket.bucketName,
                textAlign = TextAlign.Start,
            )
            Text(text = bucket.createTime.toString().substring(0, 10), fontSize = 10.sp)
            Text(text = "File Count:${bucket.objects.size}", fontSize = 10.sp)


        }


    }

}
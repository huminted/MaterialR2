package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.ui.theme.AppTheme
import com.iwakeup.r2client.data.BucketFullInfo


@Composable
fun BucketsSideList(
    modifier: Modifier,

    buckets: List<BucketFullInfo>,
    onBucketSelected: (BucketFullInfo) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    Row(modifier) {
        LazyColumn(modifier.padding(10.dp)) {
            items(buckets.size, key = { buckets[it].bucketName }) { index ->
                BucketItem(buckets[index], selectedIndex == index) {
                    selectedIndex = index
                    onBucketSelected(buckets[selectedIndex])
                }
            }

        }
        VerticalDivider(Modifier.width(1.dp), color = Color.LightGray)
    }


}


@Composable
fun BucketItem(bucket: BucketFullInfo, checked: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (checked) AppTheme.colors.secondaryContainer else Color.Transparent



    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(), shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor,
            contentColor = AppTheme.colors.primary
        )
    ) {
        Text(color = AppTheme.colors.onSecondaryContainer, minLines = 1, text = bucket.bucketName)
    }
}
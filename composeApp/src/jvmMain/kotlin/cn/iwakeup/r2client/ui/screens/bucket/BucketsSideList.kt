package cn.iwakeup.r2client.ui.screens.bucket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.iwakeup.r2client.ui.theme.AppTheme
import com.iwakeup.r2client.data.BucketFullInfo


@Composable
fun BucketsSideList(
    modifier: Modifier,
    buckets: List<BucketFullInfo>,
    onBucketSelected: (BucketFullInfo) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }

    Row(modifier) {
        LazyColumn(modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {

            stickyHeader {
                BucketListHeader(buckets.size.toString())
            }
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
fun BucketListHeader(bucketCount: String) {
    Text(
        modifier = Modifier.padding(10.dp).wrapContentWidth(),
        text = "Bucket List(${bucketCount})",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}


@Composable
fun BucketItem(bucket: BucketFullInfo, checked: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (checked) AppTheme.colors.secondaryContainer else Color.Transparent
    TextButton(
        onClick = onClick,
        modifier = Modifier.wrapContentWidth(), shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor,
            contentColor = AppTheme.colors.primary
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp).wrapContentWidth(Alignment.Start),
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.scrim,
                minLines = 1,
                text = bucket.bucketName
            )
        }
    }
}
package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.BucketBasicInfo


@Preview
@Composable
fun BucketDropdownMenu(
    buckets: List<BucketBasicInfo>,
    selectedBucket: BucketBasicInfo,
    onSelectedChange: (selectedBucket: BucketBasicInfo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedButton(
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            expanded = !expanded
        },
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(selectedBucket.bucketName, color = Color.Black)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Choose Bucket")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                buckets.forEach { it ->
                    DropdownMenuItem(
                        text = { Text(it.bucketName) },
                        onClick = {
                            onSelectedChange(it)
                            expanded = !expanded
                        }
                    )
                }


            }
        }
    }


}

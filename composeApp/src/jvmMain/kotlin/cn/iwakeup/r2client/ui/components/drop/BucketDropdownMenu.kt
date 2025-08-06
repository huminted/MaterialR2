package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.theme.AppTheme


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
        border = BorderStroke(1.dp, AppTheme.colors.onSecondaryContainer),
        shape = RoundedCornerShape(13.dp),
        onClick = {
            expanded = !expanded
        },
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = selectedBucket.bucketName,
            color = AppTheme.colors.onSecondaryContainer
        )
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Choose Bucket")
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

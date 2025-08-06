package cn.iwakeup.r2client.ui.components.form

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.iwakeup.r2client.data.BucketBasicInfo
import software.amazon.awssdk.services.s3.model.Bucket


@Composable
fun BucketConfigurationInitialForm(
    modifier: Modifier,
    bucketList: List<Bucket>,
    onSave: (List<Pair<String, String>>) -> Unit
) {
    val states = remember { mutableStateMapOf<String, TextFieldState>() }
    Column(modifier) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(bucketList.size, key = { bucketList[it].name() }) {
                val bucket = bucketList[it]
                val state = states.getOrPut(bucket.name()) { rememberTextFieldState() }
                BucketConfigurationItem(bucket.name(), state)
            }
            item {
                OutlinedButton(onClick = {
                    val result = states.map { Pair(it.key, it.value.text.toString()) }
                    onSave(result)
                }) {
                    Text("Save")
                }
            }

        }


    }

}

@Composable
fun BucketConfigurationForm(
    modifier: Modifier = Modifier,
    bucketList: List<BucketBasicInfo>,
    onSave: (List<Pair<String, String>>) -> Unit
) {
    val states = remember { mutableStateMapOf<String, TextFieldState>() }
    Column(modifier) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(bucketList.size, key = { bucketList[it].bucketName }) {
                val bucket = bucketList[it]
                val state =
                    states.getOrPut(bucket.bucketName) { rememberTextFieldState(initialText = bucket.publicURL.toString()) }
                BucketConfigurationItem(bucket.bucketName, state)
            }

            item {
                OutlinedButton(onClick = {
                    val result = states.map { Pair(it.key, it.value.text.toString()) }
                    onSave(result)
                }) {
                    Text("Save")
                }
            }
        }
    }

}


@Preview
@Composable
fun BucketConfigurationItem(bucketName: String, textFieldState: TextFieldState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Bucket:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(text = bucketName, fontSize = 16.sp)
        }
        AppThemeOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = textFieldState,
            label = "Public URL  $bucketName "
        )
    }

}








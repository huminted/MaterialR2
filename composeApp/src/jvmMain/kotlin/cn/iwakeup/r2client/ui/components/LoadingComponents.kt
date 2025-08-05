package cn.iwakeup.r2client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun InitialLoadingIndicator(modifier: Modifier = Modifier, loadingHintText: String) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
        Spacer(Modifier.height(10.dp))
        Text(loadingHintText)
    }
}


@Preview
@Composable
fun Preview() {
    InitialLoadingIndicator(loadingHintText = "加载中")
}
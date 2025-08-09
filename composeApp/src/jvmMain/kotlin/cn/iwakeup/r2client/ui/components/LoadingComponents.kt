@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package cn.iwakeup.r2client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun InitialLoadingIndicator(modifier: Modifier = Modifier, loadingHintText: String) {

    Column(
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainedLoadingIndicator(modifier = Modifier.size(100.dp))
        Spacer(Modifier.height(10.dp))
        Text(loadingHintText, color = AppTheme.colors.outline)
    }
}


@Preview
@Composable
fun Preview() {
    LoadingIndicator()
}
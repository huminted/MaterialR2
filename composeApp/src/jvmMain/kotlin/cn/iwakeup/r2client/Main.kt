package cn.iwakeup.r2client

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.iwakeup.r2client.resource.Res
import cn.iwakeup.r2client.resource.icon
import cn.iwakeup.r2client.ui.App
import org.jetbrains.compose.resources.painterResource

const val APP_NAME = "Material R2"

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1200.dp, 800.dp))
    Window(
        icon = painterResource(Res.drawable.icon),
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = APP_NAME,
    ) {
        App()
    }
}
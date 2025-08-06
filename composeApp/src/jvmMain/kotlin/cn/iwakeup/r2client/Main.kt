package cn.iwakeup.r2client

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.iwakeup.r2client.ui.App

const val APP_NAME = "MaterialR2"

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1200.dp, 800.dp))
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = APP_NAME,
    ) {
        App()
    }
}
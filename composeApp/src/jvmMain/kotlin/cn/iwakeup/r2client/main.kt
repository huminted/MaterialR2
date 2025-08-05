package cn.iwakeup.r2client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cn.iwakeup.r2client.ui.App

const val APP_NAME = "MaterialR2"

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = APP_NAME,
    ) {
        App()
    }
}
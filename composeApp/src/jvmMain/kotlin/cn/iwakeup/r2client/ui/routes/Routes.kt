package cn.iwakeup.r2client.ui.routes

import kotlinx.serialization.Serializable


sealed class Route {
    @Serializable
    open class RouteScreen

    @Serializable
    data class Splash(
        val freshInitialization: Boolean,
        val accountId: String = "",
        val accessKey: String = "",
        val secretKey: String = ""
    ) : RouteScreen()

    @Serializable
    data class Main(
        val accountId: String,
        val accessKey: String,
        val secretKey: String
    ) : RouteScreen()


    @Serializable
    object Upload : RouteScreen()

    @Serializable
    object Bucket : RouteScreen()

    @Serializable
    object Setting : RouteScreen()
}
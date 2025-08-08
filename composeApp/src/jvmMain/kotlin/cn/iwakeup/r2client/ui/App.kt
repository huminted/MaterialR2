package cn.iwakeup.r2client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cn.iwakeup.r2client.toAPIConfiguration
import cn.iwakeup.r2client.toMainRoute
import cn.iwakeup.r2client.toSplashRoute
import cn.iwakeup.r2client.ui.routes.Route
import cn.iwakeup.r2client.ui.screens.AppWrapper
import cn.iwakeup.r2client.ui.screens.SplashScreen
import cn.iwakeup.r2client.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    val navController = rememberNavController()

//    SearchScreen("", "")
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Route.Splash(true)
        ) {
            composable<Route.Splash> {
                val routeSplash: Route.Splash = it.toRoute()
                SplashScreen(routeSplash.freshInitialization, routeSplash.toAPIConfiguration()) { appConfig ->
                    navController.navigate(appConfig.apiConfiguration.toMainRoute())
                }


            }
            composable<Route.Main> {
                val routeData: Route.Main = it.toRoute()
                AppWrapper(routeData.toAPIConfiguration()) { newAPIConfiguration ->
                    navController.navigate(newAPIConfiguration.toSplashRoute(false))
                }
            }
        }
    }


}

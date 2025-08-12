package cn.iwakeup.r2client.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cn.iwakeup.r2client.toAPIConfiguration
import cn.iwakeup.r2client.toMainRoute
import cn.iwakeup.r2client.toSplashRoute
import cn.iwakeup.r2client.ui.routes.Route
import cn.iwakeup.r2client.ui.screens.AppWrapper
import cn.iwakeup.r2client.ui.screens.AppWrapperViewModel
import cn.iwakeup.r2client.ui.screens.SplashScreen
import cn.iwakeup.r2client.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Route.Splash(true)
        ) {
            composable<Route.Splash> {
                val routeSplash: Route.Splash = it.toRoute()
                val splashViewModel: SplashViewModel =
                    viewModel(viewModelStoreOwner = it, factory = SplashViewModel.Factory)
                SplashScreen(
                    splashViewModel,
                    routeSplash.freshInitialization,
                    routeSplash.toAPIConfiguration()
                ) { appConfig ->
                    navController.navigate(appConfig.apiConfiguration.toMainRoute())
                }


            }
            composable<Route.Main> {
                val routeData: Route.Main = it.toRoute()
                val appWrapperViewModel: AppWrapperViewModel =
                    viewModel(viewModelStoreOwner = it, factory = AppWrapperViewModel.Factory)
                AppWrapper(appWrapperViewModel, routeData.toAPIConfiguration()) { newAPIConfiguration ->
                    navController.navigate(newAPIConfiguration.toSplashRoute(false))
                }
            }
        }
    }


}

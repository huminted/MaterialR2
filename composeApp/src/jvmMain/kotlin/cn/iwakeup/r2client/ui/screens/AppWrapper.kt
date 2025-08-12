package cn.iwakeup.r2client.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToDrive
import androidx.compose.material.icons.filled.Bento
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.ui.components.InitialLoadingIndicator
import cn.iwakeup.r2client.ui.components.NavigationRailComponent
import cn.iwakeup.r2client.ui.components.NavigationRailItemData
import cn.iwakeup.r2client.ui.routes.*


@Composable
fun AppWrapper(
    appWrapperViewModel: AppWrapperViewModel,
    apiConfiguration: APIConfiguration,
    onSave: (apiConfiguration: APIConfiguration) -> Unit
) {
    val navController = rememberNavController()
    var selectedPage by remember { mutableStateOf<Route.RouteScreen>(Route.Search("")) }

    val uiState by appWrapperViewModel.uiState.collectAsStateWithLifecycle()

    val navigationItems = remember {
        mutableListOf<NavigationRailItemData>().apply {
            add(NavigationRailItemData("Upload", Icons.Default.AddToDrive, Route.Upload))
            add(NavigationRailItemData("Buckets", Icons.Default.Bento, Route.Bucket))
            add(NavigationRailItemData("Setting", Icons.Default.Settings, Route.Setting))
        }
    }

    LaunchedEffect(apiConfiguration) {
        appWrapperViewModel.loadBuckets()
    }

    when (uiState) {
        AppWrapperUIState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                InitialLoadingIndicator(loadingHintText = "Fetching buckets")
            }
        }

        is AppWrapperUIState.Success -> {

            val buckets = (uiState as AppWrapperUIState.Success).buckets
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRailComponent(navigationItems, selectedPage, {
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = {
                            val searchRoute = Route.Search("")
                            navController.navigate(searchRoute)
                            selectedPage = searchRoute

                        },
                    ) {
                        Icon(Icons.Filled.Search, "Floating action button.")
                    }
                }) {
                    selectedPage = it
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavHost(
                    navController = navController, startDestination = Route.Upload,
                    enterTransition = {
                        fadeIn(animationSpec = tween(300))
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(300))
                    },
                ) {
                    composable<Route.Upload> { UploadRoute(it, buckets) }
                    composable<Route.Bucket> { BucketRoute(it, buckets) }
                    composable<Route.Setting> { SettingRoute(apiConfiguration, buckets, onSave) }
                    composable<Route.Search> { SearchRoute(it, appWrapperViewModel.loadedBuckets) }
                }
            }

        }
    }


}


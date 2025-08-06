package cn.iwakeup.r2client.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToDrive
import androidx.compose.material.icons.filled.Bento
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.ui.components.InitialLoadingIndicator
import cn.iwakeup.r2client.ui.components.NavigationRailComponent
import cn.iwakeup.r2client.ui.components.NavigationRailItemData
import cn.iwakeup.r2client.ui.routes.BucketRoute
import cn.iwakeup.r2client.ui.routes.Route
import cn.iwakeup.r2client.ui.routes.SettingRoute
import cn.iwakeup.r2client.ui.routes.UploadRoute
import cn.iwakeup.r2client.ui.screens.bucket.BucketViewModel
import cn.iwakeup.r2client.ui.screens.upload.UploadViewModel


@Composable
fun AppWrapper(apiConfiguration: APIConfiguration, onSave: (apiConfiguration: APIConfiguration) -> Unit) {
    val navController = rememberNavController()
    var selectedPage by remember { mutableStateOf(Route.RouteScreen()) }

    val uploadViewModel = remember { UploadViewModel() }
    val bucketViewModel = remember { BucketViewModel() }


    val appWrapperViewModel = remember { AppWrapperViewModel() }

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
                InitialLoadingIndicator(loadingHintText = "获取Buckets中...")
            }
        }

        is AppWrapperUIState.Success -> {

            val buckets = (uiState as AppWrapperUIState.Success).buckets
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRailComponent(navigationItems, selectedPage) { it ->
                    selectedPage = it
                    navController.navigate(selectedPage)
                }
                NavHost(navController = navController, startDestination = Route.Upload) {
                    composable<Route.Upload> { UploadRoute(buckets, uploadViewModel) }
                    composable<Route.Bucket> { BucketRoute(buckets, bucketViewModel) }
                    composable<Route.Setting> { SettingRoute(apiConfiguration, buckets, onSave) }
                }
            }

        }
    }


}


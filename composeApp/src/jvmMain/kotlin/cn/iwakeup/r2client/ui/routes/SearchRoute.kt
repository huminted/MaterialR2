package cn.iwakeup.r2client.ui.routes;


import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.screens.search.SearchScreen

@Composable
fun SearchRoute(navBackStackEntry: NavBackStackEntry, buckets: List<BucketBasicInfo>) {
    val routeSearchData: Route.Search = navBackStackEntry.toRoute()
    var defaultBucket = buckets.firstOrNull {
        it.bucketName == routeSearchData.bucketName
    }
    if (defaultBucket == null) {
        defaultBucket = buckets.getOrNull(0)
    }

    defaultBucket?.let {
        SearchScreen(defaultBucket, buckets)
    }


}
package cn.iwakeup.r2client.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.ui.routes.Route


data class NavigationRailItemData(val text: String, val icon: ImageVector, val routeScreen: Route.RouteScreen)


@Composable
fun NavigationRailComponent(
    items: List<NavigationRailItemData>,
    selectedPage: Route.RouteScreen,
    onNavItemChange: (selectedPage: Route.RouteScreen) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }
    val railWidth by animateDpAsState(
        targetValue = if (isExpanded) 122.dp else 72.dp,
        label = "rail_width"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 500), // 可调动画时间
        label = "alphaAnimation"
    )

    Surface(
        tonalElevation = 4.dp,
        modifier = Modifier
            .width(railWidth)
            .fillMaxHeight()
            .animateContentSize()
    ) {
        NavigationRail(
            header = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded)
                                Icons.AutoMirrored.Default.MenuOpen
                            else
                                Icons.Default.Menu,
                            contentDescription = "切换展开"
                        )
                    }
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        onClick = { },
                    ) {
                        Icon(Icons.Filled.Search, "Floating action button.")
                    }
                    Spacer(Modifier.height(100.dp))
                }
            }
        ) {

            items.forEachIndexed { index, label ->
                val navigationData = items[index]
                NavigationRailItem(
                    modifier = Modifier.defaultMinSize(minHeight = 80.dp).wrapContentSize(),
                    selected = navigationData.routeScreen == selectedPage,
                    onClick = { onNavItemChange(navigationData.routeScreen) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = navigationData.icon,
                            contentDescription = navigationData.text
                        )

                    },
                    label = {
                        Text(modifier = Modifier.alpha(alpha), text = navigationData.text)
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}
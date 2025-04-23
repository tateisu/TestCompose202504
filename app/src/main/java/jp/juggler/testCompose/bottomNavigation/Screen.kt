package jp.juggler.testCompose.bottomNavigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import jp.juggler.testCompose.R
import jp.juggler.testCompose.ui.theme.App1Colors
import jp.juggler.testCompose.ui.theme.App1Theme

enum class Screen(
    val id: String,
    @StringRes val nameId: Int,
    val icon: ImageVector,
    val enabled: Boolean = true,
) {
    Home(
        id = "home",
        nameId = R.string.home,
        icon = Icons.Outlined.Home,
    ),
    Search(
        id = "search",
        nameId = R.string.search,
        icon = Icons.Outlined.Search,
    ),
    Profile(
        id = "profile",
        nameId = R.string.profile,
        icon = Icons.Outlined.Person,
    ),
    Disabled(
        id = "disabled",
        nameId = R.string.disabled,
        icon = Icons.Outlined.Block,
        enabled = false,
    )
}

@Composable
fun AnimatedContentScope.ScreenContent(
    @Suppress("unused")
    navBackStackEntry: NavBackStackEntry,
    screen: Screen,
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory =
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(screen.nameId),
        )
    }
}

@Composable
fun RowScope.ScreenTabStop(
    screen: Screen,
    selected: Boolean,
    navController: NavHostController,
) {
    NavigationBarItem(
        selected = selected,
        enabled = screen.enabled,
        colors = NavigationBarItemDefaults.colors().copy(
            selectedIconColor = App1Colors.textBluePrimary,
            selectedTextColor = App1Colors.textBluePrimary,
            selectedIndicatorColor = Color.Transparent,
        ),
        onClick = {
            navController.navigate(screen.id) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = { Icon(screen.icon, contentDescription = null) },
        label = { Text( stringResource(screen.nameId)) }
    )
}

@Composable
@Preview
private fun PreviewBars() {
    App1Theme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        val navController = rememberNavController()
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            for (screen in Screen.entries) {
                ScreenTabStop(
                    navController = navController,
                    screen = screen,
                    selected = screen == Screen.Search,
                )
            }
        }
    }
}

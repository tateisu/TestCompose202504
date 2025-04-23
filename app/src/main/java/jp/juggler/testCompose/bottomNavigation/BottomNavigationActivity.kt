@file:OptIn(ExperimentalMaterial3Api::class)

package jp.juggler.testCompose.bottomNavigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.juggler.testCompose.R
import jp.juggler.testCompose.ui.theme.App1Theme

class BottomNavigationActivity : ComponentActivity() {
    companion object {
        fun Context.openBottomNavigationActivity() {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityContent(
                topBarBack = { finish() },
            )
        }
    }
}

@Composable
private fun ActivityContent(
    topBarBack: () -> Unit,
) {
    App1Theme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.bottom_navigation))
                    },
                    navigationIcon = {
                        IconButton(onClick = topBarBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                ) {
                    val navBackStackEntry = navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry.value?.destination?.route
                    for (screen in Screen.entries) {
                        ScreenTabStop(
                            navController=navController,
                            screen = screen,
                            selected = currentRoute == screen.id
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.id,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                for (screen in Screen.entries) {
                    composable(screen.id) {
                        ScreenContent(
                            navBackStackEntry = it,
                            screen= screen,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ActivityContent(
        topBarBack = {},
    )
}

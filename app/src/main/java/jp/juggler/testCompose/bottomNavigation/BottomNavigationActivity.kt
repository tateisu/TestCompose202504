@file:OptIn(ExperimentalMaterial3Api::class)

package jp.juggler.testCompose.bottomNavigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import jp.juggler.testCompose.R
import jp.juggler.testCompose.bottomNavigation.ScreenFragment.Companion.createScreenFragment
import jp.juggler.testCompose.ui.theme.App1Theme
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * BottomNavigationのサンプル
 * - Note: ページ内コンテンツにAndroidViewBindingでFragmentContainerViewを使うため、
 *   FragmentActivityを継承する
 */
class BottomNavigationActivity : FragmentActivity() {
    companion object {
        fun Context.openBottomNavigationActivity() {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }
    }

    private val currentTab = MutableStateFlow(Screen.Home)
    private val tabFragments =HashMap<Screen, ScreenFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityContent(
                topBarBack = { finish() },
                sfCurrentTab = currentTab,
                createTabFragment = {
                    tabFragments.getOrPut(it) {
                        createScreenFragment(getString(it.nameId))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ActivityContent(
        topBarBack = {},
        sfCurrentTab = MutableStateFlow(Screen.Search),
        createTabFragment = { createScreenFragment("dummy") },
    )
}

/*
    Note: androidx.compose.material3.NavigationBar を使うと
    各タブのAndroidViewのライフサイクルは各タブが選択中の時だけになってしまう
    仕方ないので代替を時前で作る
 */
@Composable
private fun ActivityContent(
    sfCurrentTab: MutableStateFlow<Screen>,
    topBarBack: () -> Unit,
    createTabFragment: (Screen) -> ScreenFragment,
) {
    App1Theme {
        val stateCurrentTab = sfCurrentTab.collectAsState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                // XXX: 実際にはタブごとにアプリバーを変えたいかもしれない
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
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    tonalElevation = 4.dp,
                    modifier = Modifier,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                            .defaultMinSize(minHeight = 64.dp)
                            .selectableGroup(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        for (screen in Screen.entries) {
                            ScreenTabStop(
                                screen = screen,
                                selected = stateCurrentTab.value == screen,
                                onClick = {
                                    sfCurrentTab.value = it
                                },
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box( modifier = Modifier.fillMaxSize().padding(innerPadding) ) {
                for (screen in Screen.entries) {
                    ScreenContent(
                        screen = screen,
                        selected = stateCurrentTab.value == screen,
                        createTabFragment = createTabFragment,
                    )
                }
            }
        }
    }
}

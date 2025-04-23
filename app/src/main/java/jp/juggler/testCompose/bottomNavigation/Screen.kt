package jp.juggler.testCompose.bottomNavigation

import android.view.View
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import jp.juggler.testCompose.R
import jp.juggler.testCompose.databinding.FragmentContainerBinding
import jp.juggler.testCompose.ui.theme.App1Colors
import jp.juggler.testCompose.ui.theme.App1Theme
import jp.juggler.testCompose.utils.inTransaction
import timber.log.Timber

enum class Screen(
    // FragmentContainerViewのIDをタブ別に変える
    val fragmentContainerViewId: Int,
    // ボトムバーのアイコン
    val icon: ImageVector,
    // ボトムバーの名前の文字列リソース
    @StringRes val nameId: Int,
    // 無効状態なら偽にする
    val enabled: Boolean = true,
) {
    Home(
        fragmentContainerViewId = R.id.screen_home,
        nameId = R.string.home,
        icon = Icons.Outlined.Home,
    ),
    Search(
        fragmentContainerViewId = R.id.screen_search,
        nameId = R.string.search,
        icon = Icons.Outlined.Search,
    ),
    Profile(
        fragmentContainerViewId = R.id.screen_profile,
        nameId = R.string.profile,
        icon = Icons.Outlined.Person,
    ),
    Disabled(
        nameId = R.string.disabled,
        fragmentContainerViewId = R.id.screen_disabled,
        icon = Icons.Outlined.Block,
        enabled = false,
    )
}

@Composable
fun BoxScope.ScreenContent(
    screen: Screen,
    selected: Boolean,
    createTabFragment: (Screen) -> ScreenFragment,
) {
    val name = stringResource(screen.nameId)
    AndroidViewBinding(
        modifier = Modifier.fillMaxSize(),
        // invoked as a signal that the view is about to be attached to the composition hierarchy
        // in a different context than its original creation.
        onReset = {
            Timber.i("FragmentContainerBinding onReset $screen.")
        },
        // invoked as a signal that this view and its binding have exited the composition hierarchy entirely
        // and will not be reused again.
        // Any additional resources used by the binding should be freed at this time.
        onRelease = {
            Timber.i("FragmentContainerBinding onRelease $screen.")
        },
        factory = { inflater, parent, attachToParent ->
            Timber.i("FragmentContainerBinding inflate $screen.")
            FragmentContainerBinding.inflate(inflater, parent, attachToParent).apply {
                fragmentContainerView.id = screen.fragmentContainerViewId
            }
        },
        update = {
            var fragment: ScreenFragment? = fragmentContainerView.getFragment()
            Timber.i("FragmentContainerBinding update $screen. existing=$fragment")
            when {
                // すでに中身のFragmentが存在する
                fragment != null -> Unit
                // まだ中身のFragmentが存在しない場合、選択されるまでは作成しない
                !selected -> Unit
                // 選択されていてまだFragmentが存在しないなら中身を追加する
                else -> {
                    createTabFragment(screen).also {
                        fragmentContainerView.inTransaction { id -> replace(id, it) }
                    }
                }
            }
            // 選択状態によりVisibilityを変える
            fragmentContainerView.visibility = when {
                selected -> View.VISIBLE
                else -> View.GONE
            }
        }
    )
}

@Composable
fun RowScope.ScreenTabStop(
    screen: Screen,
    selected: Boolean,
    onClick: (Screen) -> Unit,
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
            onClick(screen)
        },
        icon = { Icon(screen.icon, contentDescription = null) },
        label = { Text(stringResource(screen.nameId)) }
    )
}

@Composable
@Preview
private fun PreviewBars() {
    App1Theme(
        darkTheme = false,
        dynamicColor = false,
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            for (screen in Screen.entries) {
                ScreenTabStop(
                    screen = screen,
                    selected = screen == Screen.Search,
                    onClick = {},
                )
            }
        }
    }
}

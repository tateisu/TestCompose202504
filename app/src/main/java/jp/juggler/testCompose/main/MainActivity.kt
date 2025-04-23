@file:OptIn(ExperimentalMaterial3Api::class)

package jp.juggler.testCompose.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.juggler.testCompose.R
import jp.juggler.testCompose.bottomNavigation.BottomNavigationActivity.Companion.openBottomNavigationActivity
import jp.juggler.testCompose.impressionExample.ImpressionExampleActivity.Companion.openImpressionExampleActivity
import jp.juggler.testCompose.ui.theme.App1Theme

class MainActivity : ComponentActivity() {

    private val menuList by lazy {
        listOf(
            MenuItem(R.string.impression_example) {
                openImpressionExampleActivity()
            },
            MenuItem(R.string.bottom_navigation) {
                openBottomNavigationActivity()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainContent(menuList)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MainContent(
        listOf(
            MenuItem(action = {}, labelId = R.string.impression_example),
        )
    )
}

@Composable
fun MainContent(list: List<MenuItem>) {
    App1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(list) { item ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.action() }
                            .padding(16.dp),
                        text = stringResource(item.labelId),
                    )
                }
            }
        }
    }
}

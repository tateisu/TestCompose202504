@file:OptIn(ExperimentalMaterial3Api::class)

package jp.juggler.testCompose.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
            MenuItem(
                labelId = R.string.impression_example,
                descId = R.string.impression_example_desc,
            ) {
                openImpressionExampleActivity()
            },
            MenuItem(
                labelId = R.string.bottom_navigation,
                descId = R.string.bottom_navigation_desc,
            ) {
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
            MenuItem(
                action = {},
                labelId = R.string.impression_example,
                descId = R.string.impression_example_desc,
            ),
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
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                )
            },
        ) { innerPadding ->
            val mapExpand = remember { mutableStateMapOf<Int, Boolean>() }
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
                    val isExpand = mapExpand[item.labelId] == true
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    ) {
                        Spacer(Modifier.weight(1f))
                        Button(
                            modifier = Modifier.defaultMinSize(48.dp),
                            onClick = { mapExpand[item.labelId] = !isExpand },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(CornerSize(4.dp)),
                        ) {
                            Text(text = stringResource(R.string.description))
                            Image(
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                imageVector = when {
                                    isExpand -> Icons.Outlined.ArrowDropDown
                                    else -> Icons.Outlined.ArrowDropUp
                                },
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isExpand,
                        enter = expandVertically(expandFrom = Alignment.Top),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top),
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 48.dp, end = 16.dp, bottom = 8.dp),
                            text = stringResource(item.descId),
                        )
                    }
                }
            }
        }
    }
}

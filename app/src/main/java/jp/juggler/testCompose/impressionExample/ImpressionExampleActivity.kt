@file:OptIn(ExperimentalMaterial3Api::class)

package jp.juggler.testCompose.impressionExample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.juggler.testCompose.R
import jp.juggler.testCompose.ui.theme.App1Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.TreeSet
import kotlin.random.Random

/**
 * ComposeでHorizontalPagerの中にLazyColumnを表示して、
 * LazyColumn内の行の表示状態の変化を追跡するサンプル。
 * ComposeVisibilityTracker の使用例
 */
class ImpressionExampleActivity : ComponentActivity() {
    companion object {
        fun Context.openImpressionExampleActivity() {
            startActivity(Intent(this, ImpressionExampleActivity::class.java))
        }
    }

    private val sfSideText = MutableStateFlow("")

    private val visibleItems = TreeSet<Item>()

    private val visibilityTracker = ComposeVisibilityTrackerImpl<Page, Item>(
        // itemの可視状態が変化すると呼び出される
        onVisibilityChanged = { added, removed ->
            Timber.i(
                "onVisibilityChanged added=${added.joinToString(",") { it.name }} removed=${
                    removed.joinToString(
                        ","
                    ) { it.name }
                }"
            )
            added.forEach { visibleItems.add(it) }
            removed.forEach { visibleItems.remove(it) }
            sfSideText.value = "visibleItems:\n${visibleItems.joinToString("\n") { it.name }}"
        },
        // ページインデクスをPageTypeに変換する
        indexToPage = { pageList.elementAtOrNull(it) },
        // LazyListItemInfo を ItemType? に変換する
        itemInfoToItem = { page, info ->
            page?.items?.find { it.name == info.key }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImpressionExampleContent(
                pages = pageList,
                topBarBack = { finish() },
                sideText = sfSideText,
                visibilityTracker = visibilityTracker,
            )
        }
    }
}

private fun generateRandomColor() = Color(
    alpha = 1f, // Fully opaque
    red = 0.5f + 0.5f * Random.nextFloat(),
    green = 0.5f + 0.5f * Random.nextFloat(),
    blue = 0.5f + 0.5f * Random.nextFloat(),
)

val pageList by lazy {
    (0 until 10).map { iPage ->
        Page(
            name = "page$iPage",
            items = (0 until 200).map { iItem ->
                Item(
                    pageIndex = iPage,
                    itemIndex = iItem,
                    name = "item${iPage}-${iItem}",
                    bgColor = generateRandomColor(),
                )
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ImpressionExampleContent(
        topBarBack = {},
        pages = pageList,
        sideText = MutableStateFlow("side text"),
        visibilityTracker = ComposeVisibilityTrackerMock(),
    )
}

@Composable
fun ImpressionExampleContent(
    topBarBack: () -> Unit,
    pages: List<Page>,
    sideText: StateFlow<String>,
    visibilityTracker: ComposeVisibilityTracker<Page, Item>,
) {
    App1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.impression_example))
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
        ) { innerPadding ->
            // ViewPagerとTextを左右に分割
            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                HorizontalPager(
                    state = rememberPagerState(initialPage = 0, pageCount = { pages.size }).also {
                        visibilityTracker.collectPagerState(it)
                    },
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                ) { iPage ->
                    val page = pages[iPage]
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .background(Color(0xffeeeeeeL))
                                .padding(16.dp),
                            text = page.name,
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = rememberLazyListState().also {
                                visibilityTracker.collectLazyListState(it, iPage)
                            },
                        ) {
                            items(
                                page.items,
                                key = { it.name },
                            ) { item ->
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(item.bgColor)
                                        .padding(16.dp),
                                    text = item.name
                                )
                            }
                        }
                    }
                }
                Text(
                    modifier = Modifier.wrapContentWidth()
                        .fillMaxHeight()
                        .background(Color(0xFFDDDDDDL)),
                    text = sideText.collectAsState().value,
                )
            }
        }
    }
}

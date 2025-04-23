package jp.juggler.testCompose.impressionExample

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * LazyColumnの行の表示状態を追跡するクラスの抽象インタフェース
 *
 * 型引数
 * - PageType ページ。HashMapのキーに使うのでhashCodeとequalsが適切に実装されていること。
 * - ItemType リスト項目。containsで使うのでhashCodeとequalsが適切に実装されていること。
 *
 * Note: PageType, ItemType の equals の計算が重い場合、
 * 型引数をStringやIntにしてインデクスやキーを表す使い方ができるだろう
 */
interface ComposeVisibilityTracker<PageType : Any, ItemType : Any> {
    val emptyItems: List<ItemType>
    // 表示中のページを取得/監視できるStateFlow
    val currentPage: StateFlow<PageType?>
    // 表示中のアイテムのキーを取得/監視できるStateFlow
    val currentItems: StateFlow<List<ItemType>>

    // PagerStateを監視する
    @SuppressLint("ComposableNaming")
    @Composable
    fun collectPagerState(state: PagerState)

    // LazyListStateを監視する
    @SuppressLint("ComposableNaming")
    @Composable
    fun collectLazyListState(state: LazyListState, i: Int)
}

// プレビュー用に何もしないComposeVisibilityTracker を作成する
class ComposeVisibilityTrackerMock<PageType : Any, ItemType : Any> :
    ComposeVisibilityTracker<PageType, ItemType> {

    override val emptyItems = emptyList<ItemType>()
    override val currentPage = MutableStateFlow<PageType?>(null)
    override val currentItems = MutableStateFlow<List<ItemType>>(emptyItems)

    @Composable
    override fun collectPagerState(state: PagerState) = Unit

    @Composable
    override fun collectLazyListState(state: LazyListState, i: Int) = Unit
}

// onVisibilityChanged を発行する ComposeVisibilityTracker
class ComposeVisibilityTrackerImpl<PageType : Any, ItemType : Any>(
    // itemの可視状態が変化すると呼び出される
    private val onVisibilityChanged: suspend (added: List<ItemType>, removed: List<ItemType>) -> Unit,
    // ページインデクスをPageTypeに変換する
    private val indexToPage: (Int) -> PageType?,
    // LazyListItemInfo を ItemType? に変換する
    private val itemInfoToItem: (PageType?, LazyListItemInfo) -> ItemType?,
) : ComposeVisibilityTracker<PageType, ItemType> {

    override val emptyItems = emptyList<ItemType>()
    override val currentPage = MutableStateFlow<PageType?>(null)
    override val currentItems = MutableStateFlow<List<ItemType>>(emptyItems)

    // ページごとの表示中アイテム
    private val pageVisibleItems = HashMap<PageType, List<ItemType>>()

    /**
     * - currentItems.valueを更新する
     * - 更新前後の差分を onVisibilityChanged で呼び出す
     */
    private suspend fun updateCurrentItems() {
        val oldItems = currentItems.value
        val newItems = currentPage.value?.let { pageVisibleItems[it] } ?: emptyItems
        val added = newItems.filter { !oldItems.contains(it) }
        val removed = oldItems.filter { !newItems.contains(it) }
        if (added.isEmpty() && removed.isEmpty()) return
        currentItems.value = newItems
        onVisibilityChanged(added, removed)
    }

    @Composable
    override fun collectPagerState(state: PagerState) {
        LaunchedEffect(state) {
            snapshotFlow { state.targetPage }
                .distinctUntilChanged()
                .collect {
                    currentPage.value = indexToPage(it)
                    updateCurrentItems()
                }
        }
    }

    @Composable
    override fun collectLazyListState(state: LazyListState, iPage: Int) {
        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo.visibleItemsInfo }
                .map { list -> list.mapNotNull { itemInfoToItem(indexToPage(iPage), it) } }
                .distinctUntilChanged()
                .collect {
                    val page = indexToPage(iPage) ?: return@collect
                    pageVisibleItems[page] = it
                    updateCurrentItems()
                }
        }
    }
}

package jp.juggler.testCompose.impressionExample

import androidx.compose.ui.graphics.Color

data class Item(
    val pageIndex: Int,
    val itemIndex: Int,
    val bgColor: Color,
    val name: String,
) : Comparable<Item> {
    override fun compareTo(other: Item): Int =
        compareValuesBy(this, other, { it.pageIndex }, { it.itemIndex })
}

package jp.juggler.testCompose.main

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
data class MenuItem(
    @StringRes val labelId: Int,
    @Stable val action: () -> Unit,
)

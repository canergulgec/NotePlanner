package com.caner.noteplanner.utils.extension

import androidx.compose.ui.unit.*

fun getMaxSp(scrollOffset: Float): TextUnit {
    return maxOf(24.0, 32.0 * scrollOffset).sp
}

fun getMaxDp(scrollOffset: Float): Dp {
    return max(24.dp, 32.dp * scrollOffset)
}
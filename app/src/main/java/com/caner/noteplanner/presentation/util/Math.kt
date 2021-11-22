package com.caner.noteplanner.presentation.util

import androidx.compose.ui.unit.*

fun getMaxSp(scrollOffset: Float): TextUnit {
    return maxOf(28.0, 40.0 * scrollOffset).sp
}

fun getIconMaxSize(scrollOffset: Float): Dp {
    return max(28.dp, 40.dp * scrollOffset)
}
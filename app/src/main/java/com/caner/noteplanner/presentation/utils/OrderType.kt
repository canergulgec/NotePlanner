package com.caner.noteplanner.presentation.utils

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
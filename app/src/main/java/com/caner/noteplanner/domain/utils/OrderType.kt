package com.caner.noteplanner.domain.utils

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
package com.caner.noteplanner.data.viewstate

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val e: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}
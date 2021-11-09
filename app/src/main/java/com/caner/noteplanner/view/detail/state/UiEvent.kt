package com.caner.noteplanner.view.detail.state

sealed class UiEvent{
    data class ShowSnackBar(val message: String?): UiEvent()
    object NoteSaved: UiEvent()
}

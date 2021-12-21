package com.caner.noteplanner.view.detail.state

data class NoteDetailUiState(
    val noteId: Int = 0,
    val titleText: String = "",
    val descriptionText: String = "",
    val noteColor: Int = -1,
    val noteSaved: Boolean = false,
    val showErrorSnack: Boolean = false
)
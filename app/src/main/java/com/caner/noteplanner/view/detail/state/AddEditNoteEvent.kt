package com.caner.noteplanner.view.detail.state

sealed class AddEditNoteEvent {
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    data class ChangeTitle(val text: String) : AddEditNoteEvent()
    data class ChangeDescription(val text: String) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
}

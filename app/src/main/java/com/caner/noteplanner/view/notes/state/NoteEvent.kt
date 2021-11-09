package com.caner.noteplanner.view.notes.state

import com.caner.noteplanner.domain.utils.NoteOrder

sealed class NoteEvent{
    data class Order(val noteOrder: NoteOrder): NoteEvent()
    data class DeleteNote(val noteId: Int): NoteEvent()
    object ToggleOrderSection: NoteEvent()
}

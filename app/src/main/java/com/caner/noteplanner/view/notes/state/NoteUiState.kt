package com.caner.noteplanner.view.notes.state

import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.domain.utils.ErrorMessage
import com.caner.noteplanner.domain.utils.NoteOrder

sealed interface NoteUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val noteOrder: NoteOrder
    val isOrderSectionVisible: Boolean

    data class NoNotes(
        override val isLoading: Boolean,
        override val noteOrder: NoteOrder,
        override val isOrderSectionVisible: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : NoteUiState

    data class HasNotes(
        val notes: List<Note>,
        override val isLoading: Boolean,
        override val noteOrder: NoteOrder,
        override val isOrderSectionVisible: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : NoteUiState
}

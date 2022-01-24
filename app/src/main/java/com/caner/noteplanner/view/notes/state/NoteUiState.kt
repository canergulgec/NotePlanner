package com.caner.noteplanner.view.notes.state

import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.utils.network.ErrorMessage
import com.caner.noteplanner.presentation.utils.NoteOrder

sealed interface NoteUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoNotes(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : NoteUiState

    data class HasNotes(
        val notes: List<Note>,
        val noteOrder: NoteOrder,
        val isOrderSectionVisible: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : NoteUiState
}

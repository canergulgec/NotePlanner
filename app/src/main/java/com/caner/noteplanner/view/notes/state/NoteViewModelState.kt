package com.caner.noteplanner.view.notes.state

import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.utils.network.ErrorMessage
import com.caner.noteplanner.presentation.utils.NoteOrder
import com.caner.noteplanner.presentation.utils.OrderType

/**
 * An internal representation of the Note route state, in a raw form
 */
data class NoteViewModelState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isOrderSectionVisible: Boolean = false,
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val errorMessages: List<ErrorMessage> = emptyList(),
) {

    /**
     * Converts this [NoteViewModelState] into a more strongly typed [NoteUiState] for driving
     * the ui.
     */
    fun toUiState(): NoteUiState =
        if (notes.isEmpty()) {
            NoteUiState.NoNotes(
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        } else {
            NoteUiState.HasNotes(
                notes = notes,
                isLoading = isLoading,
                noteOrder = noteOrder,
                isOrderSectionVisible = isOrderSectionVisible,
                errorMessages = errorMessages,
            )
        }
}
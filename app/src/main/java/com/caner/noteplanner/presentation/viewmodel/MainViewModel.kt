package com.caner.noteplanner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caner.noteplanner.R
import com.caner.noteplanner.domain.repository.NoteRepository
import com.caner.noteplanner.domain.utils.ErrorMessage
import com.caner.noteplanner.domain.utils.NoteOrder
import com.caner.noteplanner.domain.utils.OrderType
import com.caner.noteplanner.view.notes.state.NoteEvent
import com.caner.noteplanner.view.notes.state.NoteViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(NoteViewModelState(isLoading = true))

    // UI state exposed to the UI
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getAllNotes(NoteOrder.Date(OrderType.Descending))
    }

    private fun getAllNotes(noteOrder: NoteOrder) = viewModelScope.launch {
        repository.getAllNotes(noteOrder).catch {
            viewModelState.update {
                val errorMessages = it.errorMessages + ErrorMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    messageId = R.string.default_error
                )
                it.copy(errorMessages = errorMessages, isLoading = false)
            }
        }.distinctUntilChanged().collect { noteList ->
            viewModelState.update { it.copy(notes = noteList, isLoading = false) }
        }
    }

    private fun deleteNoteById(id: Int) = viewModelScope.launch {
        repository.delete(id)
        viewModelState.update { state ->
            val newNoteList = state.notes.filterNot { it.id == id }
            state.copy(notes = newNoteList)
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.ToggleOrderSection -> {
                viewModelState.update { it.copy(isOrderSectionVisible = !it.isOrderSectionVisible) }
            }

            is NoteEvent.DeleteNote -> {
                deleteNoteById(event.noteId)
            }

            is NoteEvent.Order -> {
                viewModelState.update { it.copy(noteOrder = event.noteOrder) }
                getAllNotes(event.noteOrder)
            }
        }
    }

    fun errorMessageShown(messageId: Long) {
        viewModelState.update { currentState ->
            val messages = currentState.errorMessages.filterNot { it.id == messageId }
            currentState.copy(errorMessages = messages)
        }
    }
}

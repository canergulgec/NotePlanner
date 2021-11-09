package com.caner.noteplanner.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.data.viewstate.Resource
import com.caner.noteplanner.domain.repository.NoteRepository
import com.caner.noteplanner.domain.utils.NoteOrder
import com.caner.noteplanner.domain.utils.OrderType
import com.caner.noteplanner.view.notes.state.NoteEvent
import com.caner.noteplanner.view.notes.state.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<Resource<List<Note>>>(Resource.Loading)
    val feed = _viewState.asStateFlow()

    private val _noteState = mutableStateOf(NoteState())
    val noteState: State<NoteState> = _noteState

    init {
        getAllNotes(NoteOrder.Date(OrderType.Descending))
    }

    private fun getAllNotes(noteOrder: NoteOrder) = viewModelScope.launch {
        repository.getAllNotes(noteOrder).catch { error ->
            _viewState.value = Resource.Error(error)
        }.distinctUntilChanged().collect { result ->
            if (result.isNullOrEmpty()) {
                _viewState.value = Resource.Empty
            } else {
                _viewState.value = Resource.Success(result)
            }
        }
    }

    private fun deleteNoteById(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            NoteEvent.ToggleOrderSection -> {
                _noteState.value =
                    noteState.value.copy(isOrderSectionVisible = !noteState.value.isOrderSectionVisible)
            }

            is NoteEvent.DeleteNote -> {
                deleteNoteById(event.noteId)
            }

            is NoteEvent.Order -> {
                _noteState.value = noteState.value.copy(noteOrder = event.noteOrder)
                getAllNotes(event.noteOrder)
            }
        }
    }
}

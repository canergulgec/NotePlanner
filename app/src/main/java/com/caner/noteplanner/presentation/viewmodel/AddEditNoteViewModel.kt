package com.caner.noteplanner.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caner.noteplanner.data.Constants
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.domain.repository.NoteRepository
import com.caner.noteplanner.view.detail.state.AddEditNoteEvent
import com.caner.noteplanner.view.detail.state.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _titleText = mutableStateOf("")
    val titleText: State<String> = _titleText

    private val _descriptionText = mutableStateOf("")
    val descriptionText: State<String> = _descriptionText

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>(Constants.NOTE_ID)?.let { noteId ->
            if (noteId != -1) {
                findNoteByID(noteId)
            }
        }
    }

    private fun saveNote(note: Note) = viewModelScope.launch {
        try {
            repository.insert(note)
            _eventFlow.emit(UiEvent.NoteSaved)
        } catch (e: Exception) {
            _eventFlow.emit(UiEvent.ShowSnackBar(e.message))
        }
    }

    private fun findNoteByID(id: Int) = viewModelScope.launch {
        repository.find(id).catch { _ ->


        }.distinctUntilChanged().collect { note ->
            note.apply {
                _titleText.value = title
                _descriptionText.value = description
                _noteColor.value = color
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.ChangeTitle -> {
                _titleText.value = event.text
            }

            is AddEditNoteEvent.ChangeDescription -> {
                _descriptionText.value = event.text
            }

            is AddEditNoteEvent.SaveNote -> {
                saveNote(
                    Note(
                        _titleText.value,
                        _descriptionText.value,
                        color = _noteColor.value,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
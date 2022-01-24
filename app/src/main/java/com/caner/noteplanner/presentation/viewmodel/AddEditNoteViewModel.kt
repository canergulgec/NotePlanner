package com.caner.noteplanner.presentation.viewmodel

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caner.noteplanner.utils.Constants
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.data.local.repository.NoteRepository
import com.caner.noteplanner.view.detail.state.AddEditNoteEvent
import com.caner.noteplanner.view.detail.state.NoteDetailUiState
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

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(NoteDetailUiState())
    val uiState: StateFlow<NoteDetailUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Int>(Constants.NOTE_ID)?.let { noteId ->
            if (noteId != -1) {
                findNoteByID(noteId)
            }
        }

        savedStateHandle.get<Int>(Constants.NOTE_COLOR)?.let { noteColor ->
            _uiState.update {
                if (noteColor != -1) {
                    it.copy(noteColor = noteColor)
                } else {
                    it.copy(noteColor = Note.noteColors.random().toArgb())
                }
            }
        }
    }

    private fun saveNote(note: Note) = viewModelScope.launch {
        _uiState.update {
            try {
                repository.insert(note)
                it.copy(noteSaved = true)
            } catch (e: Exception) {
                it.copy(showErrorSnack = true)
            }
        }
    }

    private fun findNoteByID(id: Int) = viewModelScope.launch {
        repository.find(id).catch {
            _uiState.update { it.copy(showErrorSnack = true) }
        }.distinctUntilChanged().collect { note ->
            note.apply {
                _uiState.update {
                    it.copy(
                        noteId = id,
                        titleText = title,
                        descriptionText = description,
                        noteColor = color
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeColor -> {
                _uiState.update { it.copy(noteColor = event.color) }
            }

            is AddEditNoteEvent.ChangeTitle -> {
                _uiState.update { it.copy(titleText = event.text) }
            }

            is AddEditNoteEvent.ChangeDescription -> {
                _uiState.update { it.copy(descriptionText = event.text) }
            }

            is AddEditNoteEvent.SaveNote -> {
                _uiState.value.apply {
                    saveNote(
                        Note(
                            id = noteId,
                            title = titleText,
                            description = descriptionText,
                            color = noteColor,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.update { it.copy(showErrorSnack = false) }
    }
}
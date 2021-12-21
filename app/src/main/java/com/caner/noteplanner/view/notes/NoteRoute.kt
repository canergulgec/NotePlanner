package com.caner.noteplanner.view.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.presentation.viewmodel.NoteViewModel
import com.caner.noteplanner.ui.components.ShowDialog
import com.caner.noteplanner.view.navigation.MainActions
import com.caner.noteplanner.view.notes.state.NoteEvent
import com.caner.noteplanner.view.notes.state.NoteUiState
import kotlin.math.min

@Composable
fun NoteRoute(
    actions: MainActions,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    NoteRoute(uiState = uiState, actions = actions)
}

@Composable
fun NoteRoute(
    uiState: NoteUiState,
    actions: MainActions,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    Scaffold(
        topBar = {
            NoteTopBar(
                scrollOffset = scrollOffset,
                isOrderSectionVisible = uiState is NoteUiState.HasNotes
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(30.dp),
                onClick = {
                    actions.gotoAddNote.invoke()
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.background,
                elevation = FloatingActionButtonDefaults.elevation(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_note),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is NoteUiState.HasNotes -> {
                    SortView(
                        uiState = uiState,
                        orderSelected = { viewModel.onEvent(NoteEvent.Order(it)) }
                    )

                    NoteList(
                        uiState = uiState,
                        actions = actions,
                        scrollState = scrollState
                    )
                }

                is NoteUiState.NoNotes -> {
                    EmptyNoteState(
                        visibility = !uiState.isLoading && uiState.errorMessages.isEmpty(),
                        lottieResource = R.raw.empty_state,
                        message = stringResource(id = R.string.empty_note_list)
                    )

                    if (uiState.errorMessages.isNotEmpty()) {
                        // Remember the errorMessage to display on the screen
                        val errorMessage = remember(uiState) { uiState.errorMessages[0] }
                        val errorMessageText = stringResource(errorMessage.messageId)

                        ShowDialog(
                            showDialog = uiState.errorMessages.isNotEmpty(),
                            message = errorMessageText,
                            dismiss = { viewModel.errorMessageShown(errorMessage.id) }
                        )
                    }
                }
            }
        }
    }
}
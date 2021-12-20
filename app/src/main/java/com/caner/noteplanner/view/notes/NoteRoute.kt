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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.presentation.viewmodel.MainViewModel
import com.caner.noteplanner.view.navigation.MainActions
import com.caner.noteplanner.view.notes.state.NoteEvent
import kotlin.math.min

@Composable
fun NoteRoute(
    actions: MainActions,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            NoteTopBar(scrollOffset = scrollOffset)
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
        val uiState by viewModel.uiState.collectAsState()

        Column(modifier = Modifier.fillMaxSize()) {
            SortView(uiState = uiState) {
                viewModel.onEvent(NoteEvent.Order(it))
            }
            NoteList(
                uiState = uiState,
                actions = actions,
                scrollState = scrollState,
                onErrorDismiss = { viewModel.errorMessageShown(it) })
        }
    }
}
package com.caner.noteplanner.view.notes

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.data.viewstate.Resource
import com.caner.noteplanner.presentation.util.getIconMaxSize
import com.caner.noteplanner.presentation.util.getMaxSp
import com.caner.noteplanner.presentation.viewmodel.MainViewModel
import com.caner.noteplanner.view.navigation.MainActions
import com.caner.noteplanner.view.notes.animation.LottieAnimationPlaceHolder
import com.caner.noteplanner.view.notes.state.NoteEvent
import kotlin.math.min

@Composable
fun NotesScreen(
    actions: MainActions,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    Scaffold(
        topBar = {
            NoteTopBar(scrollOffset)
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
            SortSection(noteOrder = viewModel.noteState.value.noteOrder) {
                viewModel.onEvent(NoteEvent.Order(it))
            }
            AddNotes(actions, scrollState)
        }

    }
}

@Composable
fun NoteTopBar(scrollOffset: Float, viewModel: MainViewModel = hiltViewModel()) {
    val animatedSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.notes),
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Start,
                fontSize = getMaxSp(scrollOffset)

            )
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.onEvent(NoteEvent.ToggleOrderSection)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(R.string.sort),
                    modifier = Modifier.size(getIconMaxSize(scrollOffset)),
                )
            }
        },
        elevation = 0.dp,
        modifier = Modifier.height(animatedSize)
    )
}


@Composable
fun AddNotes(
    actions: MainActions,
    scrollState: LazyListState,
    viewModel: MainViewModel = hiltViewModel()
) {
    when (val noteState = viewModel.feed.collectAsState().value) {
        is Resource.Success -> {
            NoteList(noteState.data, scrollState) {
                actions.gotoEditNote(it.id, it.color)
            }
        }

        is Resource.Empty -> {
            LottieAnimationPlaceHolder(
                lottie = R.raw.empty_state,
                message = stringResource(id = R.string.empty_note_list)
            )
        }

        is Resource.Error -> {
            LottieAnimationPlaceHolder(
                lottie = R.raw.error_state,
                message = noteState.throwable.message ?: ""
            )
        }
    }
}

@Composable
fun NoteList(
    noteList: List<Note>,
    scrollState: LazyListState,
    viewModel: MainViewModel = hiltViewModel(),
    noteClicked: (Note) -> (Unit)
) {
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(noteList) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(item.color), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .clickable {
                        noteClicked(item)
                    }
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {
                            viewModel.onEvent(NoteEvent.DeleteNote(item.id))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = MaterialTheme.colors.onSurface,
                            contentDescription = stringResource(R.string.delete_note),
                        )
                    }
                }
            }
        }
    }
}

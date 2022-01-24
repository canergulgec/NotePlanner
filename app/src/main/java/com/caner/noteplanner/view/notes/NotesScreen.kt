package com.caner.noteplanner.view.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.presentation.viewmodel.NoteViewModel
import com.caner.noteplanner.utils.extension.getMaxDp
import com.caner.noteplanner.utils.extension.getMaxSp
import com.caner.noteplanner.view.navigation.MainActions
import com.caner.noteplanner.view.notes.animation.LottieAnimationPlaceHolder
import com.caner.noteplanner.view.notes.state.NoteEvent
import com.caner.noteplanner.view.notes.state.NoteUiState

@Composable
fun NoteTopBar(
    scrollOffset: Float,
    isOrderSectionVisible: Boolean,
    collapsedToolbarHeight: Dp = 56.dp,
    expandedToolbarHeight: Dp = 112.dp,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val appBarSize by animateDpAsState(
        targetValue = max(
            collapsedToolbarHeight,
            expandedToolbarHeight * scrollOffset
        )
    )
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
            if (isOrderSectionVisible) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(NoteEvent.ToggleOrderSection)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = stringResource(R.string.sort),
                        modifier = Modifier.size(getMaxDp(scrollOffset)),
                    )
                }
            }
        },
        elevation = 0.dp,
        modifier = Modifier.height(appBarSize)
    )
}

@Composable
fun NoteList(
    uiState: NoteUiState.HasNotes,
    actions: MainActions,
    scrollState: LazyListState
) {
    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(uiState.notes) { item ->
            NoteItem(
                noteItem = item,
                onItemClick = { actions.gotoEditNote(item.id, item.color) }
            )
        }
    }
}

@Composable
fun NoteItem(
    noteItem: Note,
    viewModel: NoteViewModel = hiltViewModel(),
    onItemClick: (Note) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(noteItem.color), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable {
                onItemClick(noteItem)
            }
    ) {
        Text(
            text = noteItem.title,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = noteItem.description,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = {
                    viewModel.onEvent(NoteEvent.DeleteNote(noteItem.id))
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

@Composable
fun EmptyNoteState(
    visibility: Boolean,
    lottieResource: Int,
    message: String
) {
    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn(
            // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
            initialAlpha = 0.4f
        ),
        exit = fadeOut(
            // Overwrites the default animation with tween
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        LottieAnimationPlaceHolder(
            lottie = lottieResource,
            message = message
        )
    }
}


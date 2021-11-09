package com.caner.noteplanner.view.detail

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.data.model.Note
import com.caner.noteplanner.presentation.viewmodel.AddEditNoteViewModel
import com.caner.noteplanner.ui.components.TextFieldWithHint
import com.caner.noteplanner.view.navigation.MainActions
import com.caner.noteplanner.view.detail.state.AddEditNoteEvent
import com.caner.noteplanner.view.detail.state.UiEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    actions: MainActions,
    color: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val animatedBackground = remember {
        Animatable(
            Color(if (color != -1) color else viewModel.noteColor.value)
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingButton()
        }, scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedBackground.value)
        ) {
            NoteColorList { colorInt ->
                scope.launch {
                    animatedBackground.animateTo(
                        targetValue = Color(colorInt),
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                }
                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
            }

            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithHint(
                title = stringResource(id = R.string.title),
                text = viewModel.titleText.value,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitle(it))
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithHint(
                title = stringResource(id = R.string.description),
                text = viewModel.descriptionText.value,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeDescription(it))
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                childModifier = Modifier
                    .align(Alignment.Start)
                    .defaultMinSize(minHeight = 200.dp),
                singleLine = false
            )
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    event.message?.let {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = it
                        )
                    }
                }
                is UiEvent.NoteSaved -> {
                    actions.upPress.invoke()
                }
            }
        }
    }
}

@Composable
fun FloatingButton(
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    FloatingActionButton(
        modifier = Modifier.padding(30.dp),
        onClick = {
            viewModel.onEvent(AddEditNoteEvent.SaveNote)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.background,
        elevation = FloatingActionButtonDefaults.elevation(12.dp)
    ) {
        Icon(
            Icons.Default.Create,
            contentDescription = stringResource(id = R.string.add_edit_note),
            tint = MaterialTheme.colors.background
        )
    }
}

@Composable
fun NoteColorList(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    colorPicked: (Int) -> (Unit)
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = 24.dp,
            horizontal = 16.dp
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(Note.noteColors) {
            val colorInt = it.toArgb()
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(it, CircleShape)
                    .border(
                        width = 3.dp,
                        color = if (viewModel.noteColor.value == colorInt) {
                            Color.Black
                        } else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        colorPicked(colorInt)
                    }
            )
        }
    }
}

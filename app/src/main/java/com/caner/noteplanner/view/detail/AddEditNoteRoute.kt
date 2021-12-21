package com.caner.noteplanner.view.detail

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.presentation.viewmodel.AddEditNoteViewModel
import com.caner.noteplanner.view.detail.state.AddEditNoteEvent
import com.caner.noteplanner.view.navigation.MainActions
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteRoute(
    actions: MainActions,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val uiState by viewModel.uiState.collectAsState()

    val animatedBackground = remember {
        Animatable(Color(uiState.noteColor))
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
            NoteColorList(uiState.noteColor) { colorInt ->
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
                text = uiState.titleText,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitle(it))
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            TextFieldWithHint(
                title = stringResource(id = R.string.description),
                text = uiState.descriptionText,
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

    if (uiState.noteSaved) {
        actions.upPress.invoke()
    }

    if (uiState.showErrorSnack) {
        val errorMessageText: String = stringResource(R.string.default_error)
        LaunchedEffect(scaffoldState) {
            scaffoldState.snackbarHostState.showSnackbar(message = errorMessageText)
            viewModel.errorMessageShown()
        }
    }
}
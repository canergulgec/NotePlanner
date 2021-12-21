package com.caner.noteplanner.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
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
import com.caner.noteplanner.view.detail.state.AddEditNoteEvent

@Composable
fun FloatingButton(viewModel: AddEditNoteViewModel = hiltViewModel()) {
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
    noteColor: Int,
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
                        color = if (noteColor == colorInt) {
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

@Composable
fun TextFieldWithHint(
    title: String = "",
    text: String = "",
    modifier: Modifier,
    childModifier: Modifier = Modifier,
    placeHolder: String = "",
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = title, color = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = placeHolder) },
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.body1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                cursorColor = MaterialTheme.colors.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = childModifier
                .fillMaxWidth()
                .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp))
        )
    }
}

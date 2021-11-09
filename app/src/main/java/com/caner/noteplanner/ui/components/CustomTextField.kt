package com.caner.noteplanner.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
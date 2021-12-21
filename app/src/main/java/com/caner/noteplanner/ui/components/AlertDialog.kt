package com.caner.noteplanner.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.caner.noteplanner.R

@Composable
fun ShowDialog(
    showDialog: Boolean,
    title: String = stringResource(id = R.string.error_title),
    message: String,
    dismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            backgroundColor = Color.LightGray,
            onDismissRequest = {
            },
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h6
                )
            },
            confirmButton = {
                Button(
                    onClick = { dismiss.invoke() },
                ) {
                    Text(
                        text = stringResource(id = R.string.okay),
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body2
                    )
                }
            },
            text = {
                Text(
                    text = message,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body1
                )
            },
        )
    }
}
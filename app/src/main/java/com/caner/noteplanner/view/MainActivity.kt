package com.caner.noteplanner.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.caner.noteplanner.view.navigation.SetNavigation
import com.caner.noteplanner.ui.theme.NotePlannerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotePlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SetStatusBarColor()
                    SetNavigation()
                }
            }
        }
    }
}

@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.onSurface

    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = true
        )
    }
}
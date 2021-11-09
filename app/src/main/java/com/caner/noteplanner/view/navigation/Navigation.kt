package com.caner.noteplanner.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.caner.noteplanner.data.Constants
import com.caner.noteplanner.presentation.util.Screen
import com.caner.noteplanner.view.detail.AddEditNoteScreen
import com.caner.noteplanner.view.notes.NotesScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@Composable
fun SetNavigation() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)
    val actions = remember(navController) { MainActions(navController) }

    ModalBottomSheetLayout(bottomSheetNavigator) {
        AnimatedNavHost(navController, startDestination = Screen.Dashboard.route) {
            composable(
                Screen.Dashboard.route
            ) {
                NotesScreen(actions)
            }

            composable(
                Screen.AddEditNote.route,
                arguments = listOf(navArgument(Constants.NOTE_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                    navArgument(Constants.NOTE_COLOR) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                val color = it.arguments?.getInt(Constants.NOTE_COLOR) ?: -1
                AddEditNoteScreen(actions, color)
            }
        }
    }
}

class MainActions(navController: NavController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }

    val gotoEditNote: (id: Int, color: Int) -> Unit = { id, color ->
        navController.navigate(
            Screen.AddEditNote.createParamsRoute(id, color)
        )
    }

    val gotoAddNote: () -> Unit = {
        navController.navigate(Screen.AddEditNote.addNoteRoute())
    }
}
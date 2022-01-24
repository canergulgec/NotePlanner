package com.caner.noteplanner.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.caner.noteplanner.utils.Constants
import com.caner.noteplanner.view.detail.AddEditNoteRoute
import com.caner.noteplanner.view.notes.NoteRoute
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun SetNavigation() {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { MainActions(navController) }

    AnimatedNavHost(navController, startDestination = Screen.Dashboard.route) {
        composable(
            Screen.Dashboard.route
        ) {
            NoteRoute(actions)
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
            AddEditNoteRoute(actions)
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
        ){
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val gotoAddNote: () -> Unit = {
        navController.navigate(Screen.AddEditNote.addNoteRoute()){
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
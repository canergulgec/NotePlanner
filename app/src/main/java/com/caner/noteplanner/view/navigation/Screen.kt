package com.caner.noteplanner.view.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AddEditNote : Screen("detail?noteId={noteId}&noteColor={noteColor}") {
        fun addNoteRoute() = "detail"
        fun createParamsRoute(id: Int, color: Int) = "detail?noteId=${id}&noteColor=${color}"
    }
}

package com.example.animeapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Trash : Screen("trash")
    object Form : Screen("form/{animeId}") {
        fun createRoute(animeId: Int) = "form/$animeId"
    }
}
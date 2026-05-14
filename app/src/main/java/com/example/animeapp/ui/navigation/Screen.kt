package com.example.animeapp.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Form : Screen("form/{animeId}") {
        fun createRoute(animeId: Int) = "form/$animeId"
    }
}
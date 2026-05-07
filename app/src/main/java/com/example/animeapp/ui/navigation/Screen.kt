package com.example.animeapp.ui.navigation

sealed class Screen(val route: String) {
    object  Main : Screen("main")

    object  Form : Screen("form/{animeId}") {
        fun createRoute(animeId: Int? = null) = "form/$animeId"
    }

    object  Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
}
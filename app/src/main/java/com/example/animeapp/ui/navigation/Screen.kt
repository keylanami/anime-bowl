package com.example.animeapp.ui.navigation

sealed class Screen(val route: String) {
    object  Main : Screen("main")

    object  Form : Screen("form/{id}") {
        fun createRoute(id: Int? = null) = "form/$id"
    }

    object  Detail : Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
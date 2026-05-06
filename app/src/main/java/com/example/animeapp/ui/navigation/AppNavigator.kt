package com.example.animeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animeapp.ui.screen.FavoriteScreen
import com.example.animeapp.ui.screen.FormScreen
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@Composable
fun AppNavigation(viewModel: AnimeViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {

        composable(Screen.Main.route) {
            FavoriteScreen(
                viewModel = viewModel,
                onNavigateToAdd = {
                    navController.navigate(Screen.Form.createRoute(0))
                },
                onNavigateToEdit = { animeId ->
                    navController.navigate(Screen.Form.createRoute(animeId))
                }
            )
        }


        composable(
            route = Screen.Form.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0

            FormScreen(
                viewModel = viewModel,
                animeId = animeId,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
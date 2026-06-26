package com.example.animeapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animeapp.ui.components.Navbar
import com.example.animeapp.ui.components.SearchLogBottomSheet
import com.example.animeapp.ui.screen.FormScreen
import com.example.animeapp.ui.screen.HomeScreen
import com.example.animeapp.ui.screen.ProfileScreen
import com.example.animeapp.ui.screen.TrashScreen
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: AnimeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    HomeScreen(viewModel, onNavigateToForm = { anime, isEdit ->
                        viewModel.setSelectedAnimeFromApi(anime)
                        navController.navigate(Screen.Form.createRoute(if (isEdit) anime.id else 0))
                    })
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(viewModel, onNavigateToEdit = { id ->
                        navController.navigate(Screen.Form.createRoute(id))
                    }, onNavigateToTrash = {
                        navController.navigate(Screen.Trash.route)
                    })
                }
                composable(Screen.Trash.route) {
                    TrashScreen(
                        viewModel = viewModel,
                        onNavigateUp = { navController.navigateUp() }
                    )
                }
                composable(
                    route = Screen.Form.route,
                    arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("animeId") ?: 0
                    FormScreen(viewModel, animeId = id, onNavigateUp = { navController.navigateUp() })
                }
            }

            // FIX NAVBAR POSISI DI BAWAH
            if (currentRoute != Screen.Form.route) {
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Navbar(
                        currentRoute = currentRoute ?: Screen.Home.route,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onAddClick = { showBottomSheet = true }
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    topStart = BowlRadius.xl,
                    topEnd = BowlRadius.xl
                )
            ) {
                SearchLogBottomSheet(viewModel, onAnimeSelected = {
                    showBottomSheet = false
                    navController.navigate(Screen.Form.createRoute(0))
                })
            }
        }
    }
}

package com.vovan.diplomaapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vovan.diplomaapp.presentation.sensors.MonitoringScreen
import com.vovan.diplomaapp.presentation.sensorsList.DatabaseScreen
import com.vovan.diplomaapp.presentation.title.TitleScreen

@Composable
fun ComposeNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.TITLE,
) {

    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    )
    {

        composable(route = Routes.TITLE) {
            TitleScreen(
                onNavigateToNext = { direction ->
                    navController.navigate(direction) {
                        popUpTo(Routes.TITLE)
                    }
                }
            )
        }

        composable(route = Routes.MONITORING) {
            MonitoringScreen(
                viewModel = hiltViewModel(),
                onBackPressed = { navController.navigateUp() }
            )
        }

        composable(route = Routes.DATABASE) {
            DatabaseScreen(
                viewModel = hiltViewModel(),
            )
        }

    }
}


object Routes {
    const val TITLE = "title"
    const val MONITORING = "monitoring"
    const val MANAGEMENT = "management"
    const val DATABASE = "database"
}
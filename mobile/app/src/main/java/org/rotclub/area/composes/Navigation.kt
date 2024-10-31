package org.rotclub.area.composes

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.rotclub.area.screens.LoginPage
import org.rotclub.area.screens.MainScreen
import org.rotclub.area.screens.RegisterPage
import org.rotclub.area.screens.SettingsScreen

sealed class GlobalRoutes(val route: String) {
    object Login : GlobalRoutes("login")
    object Register : GlobalRoutes("register")
    object MainApp : GlobalRoutes("mainapp")
    object Settings : GlobalRoutes("settings")
}

@Composable
fun Navigation(route: GlobalRoutes) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = route.route) {
        composable(
            route = GlobalRoutes.Login.route,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            LoginPage(navController = navController)
        }
        composable(
            GlobalRoutes.Register.route,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            RegisterPage(navController = navController)
        }
        composable(
            GlobalRoutes.Settings.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(700)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(700)
                )
            }
        ) {
            SettingsScreen(navController = navController)
        }
        composable(
            GlobalRoutes.MainApp.route,
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            MainScreen(globalNavController = navController)
        }
    }
}
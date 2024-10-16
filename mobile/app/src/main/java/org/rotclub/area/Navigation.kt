package org.rotclub.area

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(route: GlobalRoutes) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = route.route) {
        composable(GlobalRoutes.Login.route) {
            LoginPage(navController = navController)
        }
        composable(GlobalRoutes.Register.route) {
            RegisterPage(navController = navController)
        }
        composable(GlobalRoutes.MainApp.route) {
            MainScreen()
        }
    }
}
package org.rotclub.area.composes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.rotclub.area.lib.GlobalRoutes
import org.rotclub.area.screens.LoginPage
import org.rotclub.area.screens.MainScreen
import org.rotclub.area.screens.RegisterPage

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
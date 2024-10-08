package org.rotclub.area

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.rotclub.area.screens.ExploreScreen
import org.rotclub.area.screens.HomeScreen
import org.rotclub.area.screens.ProfileScreen
import org.rotclub.area.screens.WorkspaceScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(BottomBarScreen.Workspace.route) {
            WorkspaceScreen()
        }
        composable(BottomBarScreen.Explore.route) {
            ExploreScreen()
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
    }
}
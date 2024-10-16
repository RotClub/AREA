package org.rotclub.area.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.rotclub.area.lib.BottomBarScreen
import org.rotclub.area.screens.ExploreScreen
import org.rotclub.area.screens.HomeScreen
import org.rotclub.area.screens.ProfileScreen
import org.rotclub.area.screens.WorkspaceScreen
import org.rotclub.area.screens.ActionScreen
import org.rotclub.area.screens.NodeScreen

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
            WorkspaceScreen(navController = navController)
        }
        composable(BottomBarScreen.Explore.route) {
            ExploreScreen()
        }
        composable(BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        composable("node_screen") {
            NodeScreen(navController = navController)
        }
        composable("action_screen") {
            ActionScreen(navController = navController)
        }
    }
}
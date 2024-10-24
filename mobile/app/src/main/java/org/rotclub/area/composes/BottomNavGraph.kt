package org.rotclub.area.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.rotclub.area.lib.BottomBarScreen
import org.rotclub.area.lib.utils.animatedSlideFullLeftComposable
import org.rotclub.area.lib.utils.animatedSlideFullTopComposable
import org.rotclub.area.lib.utils.noAnimationComposable
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
        noAnimationComposable(BottomBarScreen.Home.route) {
            HomeScreen()
        }
        noAnimationComposable(BottomBarScreen.Workspace.route) {
            WorkspaceScreen(navController = navController)
        }
        noAnimationComposable(BottomBarScreen.Explore.route,) {
            ExploreScreen()
        }
        noAnimationComposable(BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        animatedSlideFullTopComposable("node_screen") {
            NodeScreen(navController = navController)
        }
        animatedSlideFullTopComposable("action_screen") {
            ActionScreen(navController = navController)
        }
    }
}
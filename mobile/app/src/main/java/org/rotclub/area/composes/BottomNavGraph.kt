package org.rotclub.area.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.rotclub.area.lib.BottomBarScreen
import org.rotclub.area.lib.utils.animatedSlideFullTopComposable
import org.rotclub.area.lib.utils.noAnimationComposable
import org.rotclub.area.screens.ActionScreen
import org.rotclub.area.screens.ExploreScreen
import org.rotclub.area.screens.HomeScreen
import org.rotclub.area.screens.NodeScreen
import org.rotclub.area.screens.ProfileScreen
import org.rotclub.area.screens.ReactionScreen
import org.rotclub.area.screens.WorkspaceScreen


@Composable
fun BottomNavGraph(navController:NavHostController, globalNavController: NavHostController) {
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
        /*noAnimationComposable(BottomBarScreen.Explore.route,) {
            ExploreScreen()
        }*/
        noAnimationComposable(BottomBarScreen.Profile.route) {
            ProfileScreen(globalNavController = globalNavController)
        }
        animatedSlideFullTopComposable("node_screen/{program}") { backStackEntry ->
            NodeScreen(navController = navController, backStackEntry = backStackEntry)
        }
        animatedSlideFullTopComposable("action_screen") {
            ActionScreen(navController = navController)
        }
        animatedSlideFullTopComposable("reaction_screen") {
            ReactionScreen(navController = navController)
        }
    }
}
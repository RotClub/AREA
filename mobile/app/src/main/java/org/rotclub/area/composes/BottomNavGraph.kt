package org.rotclub.area.composes

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.rotclub.area.lib.BASE_DEEPLINK_URL
import org.rotclub.area.lib.BottomBarScreen
import org.rotclub.area.lib.utils.animatedSlideFullTopComposable
import org.rotclub.area.lib.utils.noAnimationComposable
import org.rotclub.area.screens.ActionScreen
import org.rotclub.area.screens.ExploreScreen
import org.rotclub.area.screens.HomeScreen
import org.rotclub.area.screens.NodeScreen
import org.rotclub.area.screens.ProfileScreen
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
        noAnimationComposable(BottomBarScreen.Explore.route,) {
            ExploreScreen()
        }
        noAnimationComposable(
            BottomBarScreen.Profile.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = BASE_DEEPLINK_URL + "app/redirect/"
                    action = Intent.ACTION_VIEW
                })
            ) { entry ->
            ProfileScreen(globalNavController = globalNavController, dataEntry = entry)
        }
        animatedSlideFullTopComposable("node_screen") {
            NodeScreen(navController = navController)
        }
        animatedSlideFullTopComposable("action_screen") {
            ActionScreen(navController = navController)
        }
    }
}
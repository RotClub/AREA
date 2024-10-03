package org.rotclub.area

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Workspace,
        BottomBarScreen.Explore,
        BottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar (
        containerColor = Color.Black,
        contentColor = Color.Magenta,
    ) {
        screens.forEach { screen ->
            AddItems(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItems(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        colors = NavigationBarItemColors(
            selectedIconColor = Color.Magenta,
            selectedTextColor = Color.Magenta,
            selectedIndicatorColor = Color.Black,
            unselectedIconColor = Color.White,
            unselectedTextColor = Color.White,
            disabledIconColor = Color.Gray,
            disabledTextColor = Color.Gray
        ),
        onClick = {
            navController.navigate(screen.route)
        }
    )
}
package org.rotclub.area.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.rotclub.area.ui.theme.FrispyTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import org.rotclub.area.lib.BottomBarScreen
import org.rotclub.area.composes.BottomNavGraph

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
        containerColor = FrispyTheme.Surface500,
        contentColor = FrispyTheme.Primary500,
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
    val icon = painterResource(id = screen.iconResId)
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = { Icon(painter = icon, contentDescription = "Navigation Icon", modifier = Modifier.size(30.dp)) },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        colors = NavigationBarItemColors(
            selectedIconColor = FrispyTheme.Primary500,
            selectedTextColor = FrispyTheme.Primary500,
            selectedIndicatorColor = FrispyTheme.Surface500,
            unselectedIconColor = FrispyTheme.Primary50,
            unselectedTextColor = FrispyTheme.Primary50,
            disabledIconColor = FrispyTheme.Surface100,
            disabledTextColor = FrispyTheme.Surface100,
        ),
        onClick = {
            navController.navigate(screen.route)
        }
    )
}
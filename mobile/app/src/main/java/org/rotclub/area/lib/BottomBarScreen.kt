package org.rotclub.area.lib

import org.rotclub.area.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val iconResId: Int
) {
    data object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        iconResId = R.drawable.frispyicon
    )
    data object Workspace : BottomBarScreen(
        route = "workspace",
        title = "Workspace",
        iconResId = R.drawable.layers
    )
    data object Explore : BottomBarScreen(
        route = "explore",
        title = "Explore",
        iconResId = R.drawable.telescope
    )
    data object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        iconResId = R.drawable.user
    )
}
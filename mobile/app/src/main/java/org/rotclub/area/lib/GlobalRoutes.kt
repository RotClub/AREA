package org.rotclub.area.lib

sealed class GlobalRoutes(val route: String) {
    object Login : GlobalRoutes("login")
    object Register : GlobalRoutes("register")
    object MainApp : GlobalRoutes("mainapp")
}

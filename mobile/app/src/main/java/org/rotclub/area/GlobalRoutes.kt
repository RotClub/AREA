package org.rotclub.area

sealed class GlobalRoutes(val route: String) {
    object Login : GlobalRoutes("login")
    object Register : GlobalRoutes("register")
    object MainApp : GlobalRoutes("mainapp")
}
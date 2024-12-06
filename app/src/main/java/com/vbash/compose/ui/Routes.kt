package com.vbash.compose.ui

sealed class Screens(val route: String) {

    data object Splash : Screens("splash")
    data object Auth : Screens("auth_route")
    data object Home : Screens("home_route")

}
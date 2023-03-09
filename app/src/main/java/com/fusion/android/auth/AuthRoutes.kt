package com.fusion.android.auth

sealed class AuthRoutes(val route: String) {
    object Login: AuthRoutes("Login")
    object Chat: AuthRoutes("Chat")
}
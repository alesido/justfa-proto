package com.fusion.android.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 *      Application's entry and router point
 */
@Composable
fun AuthDispatcher() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthRoutes.Login.route) {
        composable(AuthRoutes.Login.route) { LoginPage() }
    }
}
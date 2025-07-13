package com.example.proyectofinalcarwash.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofinalcarwash.pages.authScreens.LoginScreen
import com.example.proyectofinalcarwash.pages.authScreens.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { username, password ->
                    // Aquí podrías validar credenciales y navegar a Home si es exitoso
                    navController.navigate("home")
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack() // vuelve al login
                },
                onSuccessRegister = {
                    navController.navigate("home") // o como prefieras
                }
            )
        }

        composable("home") {
            // Agrega tu HomeScreen aquí cuando lo tengas listo
        }
    }
}

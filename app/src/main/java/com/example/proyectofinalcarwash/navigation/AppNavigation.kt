package com.example.proyectofinalcarwash.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinalcarwash.ui.splash.SplashScreen
import com.example.proyectofinalcarwash.pages.authScreens.LoginScreen
import com.example.proyectofinalcarwash.pages.authScreens.RegisterScreen
import com.example.proyectofinalcarwash.home.HomeScreen
import com.example.proyectofinalcarwash.screens.ServicesScreen
import com.example.proyectofinalcarwash.screens.AppointmentScreen
import com.example.proyectofinalcarwash.screens.Servicio
import com.example.proyectofinalcarwash.screens.CalendarScreen
import com.example.proyectofinalcarwash.screens.ProfileScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectofinalcarwash.ui.components.MainLayout

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(
                onSuccessLogin = {
                    navController.navigate("home") // cuando el login sea exitoso
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
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }
        composable("services") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                ServicesScreen(
                    modifier = Modifier.padding(innerPadding),
                    /* DATOS DE PRUEBA UNICAMENTE !!!!!!!!! */
                    servicios = listOf(
                        Servicio(1, "Lavado completo", "Incluye lavado exterior e interior del vehículo", 15.0),
                        Servicio(2, "Pulido de pintura", "Mejora el brillo de la pintura y elimina rayones leves", 25.0),
                        Servicio(3, "Cambio de aceite", "Incluye cambio de aceite y filtro", 40.0)
                    )
                )
            }
        }
        composable("agendarCita") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                AppointmentScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }
        composable("calendar") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                CalendarScreen() // sin navController aquí
            }
        }
        composable("profile") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                ProfileScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }
    }
}
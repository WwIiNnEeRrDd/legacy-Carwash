package com.example.proyectofinalcarwash.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalcarwash.ui.splash.SplashScreen
import com.example.proyectofinalcarwash.pages.authScreens.LoginScreen
import com.example.proyectofinalcarwash.pages.authScreens.RegisterScreen
import com.example.proyectofinalcarwash.home.HomeScreen
import com.example.proyectofinalcarwash.screens.*
import com.example.proyectofinalcarwash.ui.components.MainLayout
import com.example.proyectofinalcarwash.viewmodel.ServiciosViewModel
import androidx.compose.runtime.LaunchedEffect

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
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSuccessRegister = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
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
            val serviciosViewModel: ServiciosViewModel = viewModel()
            val servicios = serviciosViewModel.servicios.collectAsState()

            MainLayout(navController, currentDestination) { innerPadding ->
                ServicesScreen(
                    modifier = Modifier.padding(innerPadding),
                )
            }

            // Lanzamos la carga si aún no está hecha
            LaunchedEffect(true) {
                serviciosViewModel.fetchServicios()
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
                CalendarScreen()
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

        composable("vehiculos") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(navController, currentDestination) { innerPadding ->
                MisVehiculosScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }
        }

        composable("agregarVehiculo") {
            AgregarVehiculoScreen(navController = navController)
        }
    }
}

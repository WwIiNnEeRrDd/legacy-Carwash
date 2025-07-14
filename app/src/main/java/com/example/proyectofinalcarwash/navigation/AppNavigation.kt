package com.example.proyectofinalcarwash.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
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
            MainLayout(
                navController = navController,
                currentDestination = currentDestination,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate("agendarCita") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Cita",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            ) { innerPadding ->
                CalendarScreen(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable(
            "detalleCita/{fecha}/{hora}/{servicio}/{vehiculo}/{duracionMin}"
        ) { backStackEntry ->
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val servicio = backStackEntry.arguments?.getString("servicio") ?: ""
            val vehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            val duracionMin = backStackEntry.arguments?.getString("duracionMin")?.toIntOrNull() ?: 0

            DetalleCitaScreen(
                navController = navController,
                fechaStr = fecha,
                hora = hora,
                servicio = servicio,
                vehiculo = vehiculo,
                duracionMin = duracionMin
            )
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

        composable("editarPerfil") {
            EditarPerfilScreen(navController = navController)
        }

        composable("vehicle") {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination
            MainLayout(
                navController,
                currentDestination,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate("agregarVehiculo") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Agregar Vehículo",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            ) { innerPadding ->
                MisVehiculosScreen(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable("agregarVehiculo") {
            AgregarVehiculoScreen(navController = navController)
        }
    }
}

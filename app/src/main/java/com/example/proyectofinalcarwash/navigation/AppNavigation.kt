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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.proyectofinalcarwash.promotions.PromocionesPorServicioScreen
import com.example.proyectofinalcarwash.promotions.PromotionScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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

            MainLayout(
                navController = navController,
                currentDestination = currentDestination
            ) { innerPadding ->
                ServicesScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController
                )
            }

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
            route = "detalleCita/{idCita}/{fecha}/{hora}/{servicio}/{vehiculo}/{duracionMin}/{estado}?comentario={comentario}",
            arguments = listOf(
                navArgument("idCita") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType },
                navArgument("servicio") { type = NavType.StringType },
                navArgument("vehiculo") { type = NavType.StringType },
                navArgument("duracionMin") { type = NavType.IntType },
                navArgument("estado") { type = NavType.StringType },
                navArgument("comentario") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val idCita = backStackEntry.arguments?.getInt("idCita") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            val servicio = backStackEntry.arguments?.getString("servicio") ?: ""
            val vehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            val duracionMin = backStackEntry.arguments?.getInt("duracionMin") ?: 0
            val estado = backStackEntry.arguments?.getString("estado") ?: ""
            val comentario = backStackEntry.arguments?.getString("comentario") ?: ""

            DetalleCitaScreen(
                navController = navController,
                idCita = idCita,
                fechaStr = fecha,
                hora = hora,
                servicio = servicio,
                vehiculo = vehiculo,
                duracionMin = duracionMin,
                estado = estado,
                comentario = comentario
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

        composable("historial") {
            HistorialScreen(navController)
        }

        composable(
            "promotion?text={promotionText}",
            arguments = listOf(
                navArgument("promotionText") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val promotionText = backStackEntry.arguments?.getString("promotionText") ?: ""
            PromotionScreen(
                navController = navController,
                promotionText = promotionText
            )
        }

        composable(
            route = "promocionesServicio/{idServicio}/{nombreServicio}",
            arguments = listOf(
                navArgument("idServicio") { type = NavType.IntType },
                navArgument("nombreServicio") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idServicio = backStackEntry.arguments?.getInt("idServicio") ?: 0
            val nombreServicioEncoded = backStackEntry.arguments?.getString("nombreServicio") ?: ""
            val nombreServicio = URLDecoder.decode(nombreServicioEncoded, StandardCharsets.UTF_8.toString())
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
                            contentDescription = "Agendar Cita",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            ) { innerPadding ->
                PromocionesPorServicioScreen(
                    navController = navController,
                    idServicio = idServicio,
                    nombreServicio = nombreServicio
                )
            }
        }
    }
}

package com.example.proyectofinalcarwash.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinalcarwash.home.HomeScreen
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavItem("Inicio", Icons.Default.Home, "home"),
                    BottomNavItem("Servicios", Icons.Default.Build, "services"),
                    BottomNavItem("Perfil", Icons.Default.Person, "profile") // Este puedes crear luego
                )

                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigate(item.route)
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "Bienvenido, user", /* nombre random de prueba */
                style = MaterialTheme.typography.headlineSmall
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Próxima cita", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📅 Fecha: 2025-07-20")
                    Text("🕒 Hora: 10:30 AM")
                    Text("🚗 Vehículo: Toyota Corolla")
                    Text("🧽 Servicio: Lavado completo")
                    Text("📌 Estado: Confirmada")
                }
            }

            Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium)

            // Nueva grilla de acciones rápidas estilo Speed Dial
            val acciones = listOf(
                Triple("Agendar cita", Icons.Default.Add, "agendarCita"),
                Triple("Mis vehículos", Icons.Default.DirectionsCar, "vehicle"),
                Triple("Ver servicios", Icons.Default.Build, "services"),
                Triple("Historial", Icons.Default.History, "historial")
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(acciones.size) { index ->
                    val (titulo, icono, ruta) = acciones[index]
                    Card(
                        onClick = { navController.navigate(ruta) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f), // Cuadrado
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                icono,
                                contentDescription = titulo,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(titulo, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
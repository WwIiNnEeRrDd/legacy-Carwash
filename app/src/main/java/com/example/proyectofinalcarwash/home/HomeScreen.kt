package com.example.proyectofinalcarwash.home

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.R
import com.example.proyectofinalcarwash.viewmodel.CitasViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val nombreUsuario = remember { prefs.getString("nombre", "usuario") ?: "usuario" }

    val viewModel: CitasViewModel = viewModel()
    val proximaCita by viewModel.proximaCita.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProximaCita()
    }

    val promociones = listOf(
        "15% de descuento en Lavado Premium hasta el 20 de julio",
        "Promoción: 2x1 en Pulido de pintura este fin de semana",
        "Nuevo servicio disponible: Desinfección interior"
    )

    val tips = listOf(
        "Lava tu coche cada 2 semanas para evitar acumulación de suciedad.",
        "Usa cera líquida después del lavado para proteger la pintura.",
        "Evita dejar el coche al sol tras un lavado para prevenir manchas.",
        "Limpia el interior al menos 1 vez al mes para conservar los materiales."
    )

    Scaffold(
        bottomBar = {
            val items = listOf(
                BottomNavItem("Inicio", Icons.Default.Home, "home"),
                BottomNavItem("Servicios", Icons.Default.Build, "services"),
                BottomNavItem("Perfil", Icons.Default.Person, "profile")
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp)
                    )
                    Text("Bienvenido, $nombreUsuario", style = MaterialTheme.typography.headlineSmall)
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Próxima cita", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (proximaCita != null) {
                            val cita = proximaCita!!
                            Text("📅 Fecha: ${cita.fecha_cita.take(10)}")
                            Text("🕒 Hora: ${cita.hora_cita.take(5)}")
                            Text("🚗 Vehículo: ${cita.placa}")
                            Text("🧽 Servicio: ${cita.nombre_servicio}")
                            Text("📌 Estado: ${cita.estado}")
                        } else {
                            Text("No tienes citas próximas agendadas.")
                        }
                    }
                }
            }

            item {
                Text("📢 Promociones", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(promociones.size) { index ->
                        Card(
                            modifier = Modifier
                                .width(260.dp)
                                .height(120.dp)
                                .clickable {
                                    navController.navigate("promotion/${Uri.encode(promociones[index])}")
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Text(promociones[index])
                            }
                        }
                    }
                }
            }

            item {
                Text("💡 Tips de mantenimiento", style = MaterialTheme.typography.titleMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tips.size) { index ->
                        Card(
                            modifier = Modifier
                                .width(260.dp)
                                .height(120.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Text(tips[index])
                            }
                        }
                    }
                }
            }

            item {
                Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium)
            }

            item {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    items(acciones.size) { index ->
                        val (titulo, icono, ruta) = acciones[index]
                        Card(
                            onClick = { navController.navigate(ruta) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
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
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

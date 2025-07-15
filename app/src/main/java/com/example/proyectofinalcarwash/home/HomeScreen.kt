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
import com.example.proyectofinalcarwash.viewmodel.PromocionesViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.lazy.LazyColumn
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val nombreUsuario = remember { prefs.getString("nombre", "usuario") ?: "usuario" }

    val citasViewModel: CitasViewModel = viewModel()
    val proximaCita by citasViewModel.proximaCita.collectAsState()

    val promocionesViewModel: PromocionesViewModel = viewModel()
    val promociones by promocionesViewModel.promociones.collectAsState()
    val errorPromos by promocionesViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        citasViewModel.fetchProximaCita()
        promocionesViewModel.fetchPromociones()
    }

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
                if (proximaCita != null) {
                    val cita = proximaCita!!
                    val fechaCorta = cita.fecha_cita.substring(0, 10)
                    val horaEncoded = URLEncoder.encode(cita.hora_cita, StandardCharsets.UTF_8.toString())
                    val servicioEncoded = URLEncoder.encode(cita.nombre_servicio, StandardCharsets.UTF_8.toString())
                    val placaEncoded = URLEncoder.encode(cita.placa, StandardCharsets.UTF_8.toString())
                    val estadoEncoded = URLEncoder.encode(cita.estado, StandardCharsets.UTF_8.toString())
                    val comentarioEncoded = URLEncoder.encode(cita.comentario_cliente ?: "", StandardCharsets.UTF_8.toString())

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "detalleCita/${cita.id_cita}/$fechaCorta/$horaEncoded/$servicioEncoded/$placaEncoded/${cita.duracion_estimada}/$estadoEncoded?comentario=$comentarioEncoded"
                                )
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Próxima cita", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("📅 Fecha: ${fechaCorta}")
                            Text("🕒 Hora: ${cita.hora_cita.take(5)}")
                            Text("🚗 Vehículo: ${cita.placa}")
                            Text("🧽 Servicio: ${cita.nombre_servicio}")
                            Text("📌 Estado: ${cita.estado}")
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Próxima cita", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No tienes citas próximas agendadas.")
                        }
                    }
                }
            }

            item {
                Text("📢 Promociones", style = MaterialTheme.typography.titleMedium)

                if (errorPromos != null) {
                    Text("Error al cargar promociones: $errorPromos", color = MaterialTheme.colorScheme.error)
                } else if (promociones.isEmpty()) {
                    Text("No hay promociones disponibles.")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(promociones.size) { index ->
                            val promo = promociones[index]
                            val texto = "🎉 ${promo.nombre_promocion}\n🧽 ${promo.nombre_servicio}\n💬 ${promo.descripcion}\n🔖 ${promo.descuento_porcentaje}% OFF"
                            Card(
                                modifier = Modifier
                                    .width(280.dp)
                                    .height(150.dp)
                                    .clickable {
                                        navController.navigate("promotion?text=${Uri.encode(texto)}")
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("🎉 ${promo.nombre_promocion}")
                                    Text("🧽 ${promo.nombre_servicio}")
                                    Text("💬 ${promo.descripcion}")
                                    Text("🔖 ${promo.descuento_porcentaje}% OFF")
                                }
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

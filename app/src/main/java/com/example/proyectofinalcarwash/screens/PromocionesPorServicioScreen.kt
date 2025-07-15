package com.example.proyectofinalcarwash.promotions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalcarwash.viewmodel.PromocionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromocionesPorServicioScreen(
    navController: NavController,
    idServicio: Int,
    nombreServicio: String,
    viewModel: PromocionesViewModel = viewModel()
){
    val promociones by viewModel.promociones.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(idServicio) {
        viewModel.fetchPromocionesPorServicio(idServicio)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Promociones: $nombreServicio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Puedes cambiar esta navegación para pasar idServicio si lo necesitas
                    navController.navigate("agendarCita")
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agendar Cita"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            } else if (promociones.isEmpty()) {
                Text("No hay promociones disponibles para este servicio.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(promociones) { promo ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = promo.nombre_promocion,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = promo.descripcion ?: "Sin descripción",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Descuento: ${promo.descuento_porcentaje}%",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

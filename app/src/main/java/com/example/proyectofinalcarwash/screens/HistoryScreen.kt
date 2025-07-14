package com.example.proyectofinalcarwash.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalcarwash.viewmodel.CitasViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: CitasViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val citasState = viewModel.citas.collectAsState()
    val errorState = viewModel.error.collectAsState()

    val citas = citasState.value
    val error = errorState.value

    LaunchedEffect(Unit) {
        viewModel.fetchCitas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Citas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
        } else if (citas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay citas registradas.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    val colorEstado = when (cita.estado.lowercase()) {
                        "pendiente" -> MaterialTheme.colorScheme.secondary
                        "realizada" -> MaterialTheme.colorScheme.tertiary
                        "cancelada" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.outline
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("📅 Fecha: ${cita.fecha_cita.take(10)}")
                            Text("🕒 Hora: ${cita.hora_cita}")
                            Text("🧽 Servicio: ${cita.nombre_servicio}")
                            Text("🚗 Vehículo: ${cita.placa}")
                            Text("🗨️ Comentario: ${cita.comentario_cliente}")
                            Text("📌 Estado: ${cita.estado}",
                                color = colorEstado,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


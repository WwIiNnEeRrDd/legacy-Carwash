package com.example.proyectofinalcarwash.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    navController: NavController,
    fechaStr: String,
    hora: String,
    servicio: String,
    vehiculo: String,
    duracionMin: Int
) {
    val fecha = LocalDate.parse(fechaStr)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📅 Fecha: ${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "🕒 Hora: $hora",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "🧽 Servicio: $servicio",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "🚗 Vehículo: $vehiculo",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "⏳ Duración: $duracionMin minutos",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver")
            }
        }
    }
}

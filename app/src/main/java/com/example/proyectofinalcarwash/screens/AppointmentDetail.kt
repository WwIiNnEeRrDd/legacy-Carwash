package com.example.proyectofinalcarwash.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.proyectofinalcarwash.viewmodel.CitasViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    navController: NavController,
    idCita: Int,
    fechaStr: String,
    hora: String,
    servicio: String,
    vehiculo: String,
    estado: String,
    modifier: Modifier = Modifier,
    comentario: String = "",
    duracionMin: Int
) {
    val citasViewModel: CitasViewModel = viewModel()
    val context = LocalContext.current
    val fecha = runCatching { LocalDate.parse(fechaStr) }.getOrElse {
        navController.popBackStack()
        return
    }

    val servicioDecoded = URLDecoder.decode(servicio, StandardCharsets.UTF_8.toString())
    val vehiculoDecoded = URLDecoder.decode(vehiculo, StandardCharsets.UTF_8.toString())
    val estadoDecoded = URLDecoder.decode(estado, StandardCharsets.UTF_8.toString())
    val comentarioDecoded = URLDecoder.decode(comentario, StandardCharsets.UTF_8.toString())

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
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("📅 Fecha: ${fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
            Text("🕒 Hora: $hora")
            Text("🧽 Servicio: $servicioDecoded")
            Text("🚗 Vehículo: $vehiculoDecoded")
            Text("⏳ Duración: $duracionMin minutos")
            Text("📌 Estado: ${estadoDecoded.replaceFirstChar { it.uppercaseChar() }}")

            if (comentarioDecoded.isNotBlank()) {
                Text("📝 Comentario: $comentarioDecoded")
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (estadoDecoded.equals("pendiente", ignoreCase = true)) {
                Button(
                    onClick = {
                        citasViewModel.cancelarCita(
                            idCita = idCita,
                            onSuccess = { navController.popBackStack() },
                            onError = { errorMsg ->
                                // Aquí podrías mostrar un snackbar con el mensaje si deseas
                                println("Error al cancelar la cita: $errorMsg")
                            }
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar Cita")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver")
            }
        }
    }
}

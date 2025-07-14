package com.example.proyectofinalcarwash.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.viewmodel.VehiculosViewModel
import com.example.proyectofinalcarwash.data.model.Vehiculo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisVehiculosScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: VehiculosViewModel = viewModel()
) {
    val context = LocalContext.current
    val vehiculos = viewModel.vehiculos.collectAsState().value
    val error = viewModel.error.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchVehiculos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Vehículos") }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (error != null) {
                    item {
                        Text(
                            "Error: $error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                if (vehiculos.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Aún no tienes vehículos registrados.",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(vehiculos) { vehiculo ->
                        VehiculoCard(
                            vehiculo = vehiculo,
                            onEliminar = { id ->
                                viewModel.eliminarVehiculo(
                                    idVehiculo = id,
                                    onSuccess = {
                                        Toast
                                            .makeText(context, "Vehículo eliminado", Toast.LENGTH_SHORT)
                                            .show()
                                    },
                                    onError = { msg ->
                                        Toast
                                            .makeText(context, msg, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun VehiculoCard(
    vehiculo: Vehiculo,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar vehículo") },
            text = { Text("¿Estás seguro de que deseas eliminar este vehículo?") },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(vehiculo.id)
                    showDialog = false
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("${vehiculo.marca} ${vehiculo.modelo}", fontSize = 18.sp)
                    Text("Placa: ${vehiculo.placa}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar vehículo",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

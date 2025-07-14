package com.example.proyectofinalcarwash.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                            color = MaterialTheme.colorScheme.error
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
                        VehiculoCard(vehiculo)
                    }
                }
            }
        }
    )
}

@Composable
fun VehiculoCard(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
    }
}

package com.example.proyectofinalcarwash.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.data.model.VehiculoRequest
import com.example.proyectofinalcarwash.viewmodel.VehiculosViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarVehiculoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    val context = LocalContext.current

    var placa by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agregar vehículo") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it },
                label = { Text("Placa") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                )
            )

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (placa.isBlank() || marca.isBlank() || modelo.isBlank() || color.isBlank()) {
                        Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val request = VehiculoRequest(placa, marca, modelo, color)

                    vehiculosViewModel.crearVehiculo(
                        vehiculoRequest = request,
                        onSuccess = {
                            Toast.makeText(context, "Vehículo registrado correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar vehículo")
            }
        }
    }
}

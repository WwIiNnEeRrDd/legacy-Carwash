package com.example.proyectofinalcarwash.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Cliente
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Leer datos locales al iniciar
    var nombre by remember { mutableStateOf(prefs.getString("nombre", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("email", "") ?: "") }
    var telefono by remember { mutableStateOf(prefs.getString("telefono", "") ?: "") }
    var residencia by remember { mutableStateOf(prefs.getString("residencia", "") ?: "") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Perfil") }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = residencia,
                onValueChange = { residencia = it },
                label = { Text("Residencia") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val api = RetrofitClient.create(context)
                    val cliente = Cliente(
                        nombre = nombre,
                        email = email,
                        telefono = telefono,
                        residencia = residencia
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = api.actualizarPerfil(cliente)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val clienteActualizado = response.body()?.cliente
                                    clienteActualizado?.let {
                                        // Guardar datos localmente
                                        prefs.edit()
                                            .putString("nombre", it.nombre)
                                            .putString("email", it.email)
                                            .putString("telefono", it.telefono)
                                            .putString("residencia", it.residencia)
                                            .apply()

                                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } ?: run {
                                        Toast.makeText(context, "Error al leer respuesta", Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Error ${response.code()}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

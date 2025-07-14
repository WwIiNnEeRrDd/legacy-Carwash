package com.example.proyectofinalcarwash.pages.authScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinalcarwash.R
import com.example.proyectofinalcarwash.viewmodel.RegisterViewModel
import com.example.proyectofinalcarwash.viewmodel.Result

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    onSuccessRegister: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val nombre = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        registerState?.let { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    onSuccessRegister()
                }
                is Result.Failure -> {
                    Toast.makeText(context, result.exception.message ?: "Error en el registro", Toast.LENGTH_LONG).show()
                }
                else -> Unit // para exhaustividad
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Legacy Carwash",
                modifier = Modifier.height(180.dp).padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    val icon = if (passwordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(icon, contentDescription = null)
                    }
                }
            )

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                        Icon(icon, contentDescription = null)
                    }
                }
            )

            Button(
                onClick = {
                    if (nombre.value.isBlank() || email.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank()) {
                        Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    } else if (password.value != confirmPassword.value) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.register(
                            nombre = nombre.value,
                            email = email.value,
                            contraseña = password.value,
                            telefono = "" // si no se pide en UI, ponlo vacío
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Registrarme")
            }

            TextButton(onClick = onNavigateToLogin) {
                Text("Ya tengo una cuenta")
            }
        }
    }
}

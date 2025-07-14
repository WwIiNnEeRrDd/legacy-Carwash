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
import com.example.proyectofinalcarwash.viewmodel.LoginViewModel
import com.example.proyectofinalcarwash.viewmodel.LoginResult

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSuccessLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (val result = loginState) {
            is LoginResult.Success -> {
                val token = result.data.token
                // Guardar token localmente si lo necesitas
                Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                onSuccessLogin()
            }
            is LoginResult.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }
            LoginResult.Idle, LoginResult.Loading -> Unit
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
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
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

            Button(
                onClick = {
                    if (username.value.isBlank() || password.value.isBlank()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.login(username.value.trim(), password.value)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Iniciar Sesión")
            }

            TextButton(onClick = onRegisterClick) {
                Text("¿No tienes cuenta? Registrarse")
            }
        }
    }
}

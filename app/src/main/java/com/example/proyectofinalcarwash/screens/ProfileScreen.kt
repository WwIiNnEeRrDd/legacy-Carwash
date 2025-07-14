package com.example.proyectofinalcarwash.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Cargar datos del cliente desde SharedPreferences
    val nombre = prefs.getString("nombre", "") ?: ""
    val email = prefs.getString("email", "") ?: ""
    val telefono = prefs.getString("telefono", "") ?: ""
    val residencia = prefs.getString("residencia", "") ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Cliente") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Medium)

            // Información de contacto
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                InfoRow(icon = Icons.Default.Email, text = email)
                InfoRow(icon = Icons.Default.Home, text = residencia)
                InfoRow(icon = Icons.Default.Phone, text = telefono)
            }

            Divider()

            Text("Configuración", style = MaterialTheme.typography.titleMedium)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SettingCard(icon = Icons.Default.DirectionsCar, label = "Mis Vehículos") {
                    navController.navigate("vehiculos")
                }
                SettingCard(icon = Icons.Default.History, label = "Historial de Citas") {
                    navController.navigate("historial")
                }
                SettingCard(icon = Icons.Default.Edit, label = "Editar Perfil") {
                    navController.navigate("editarPerfil")
                }
                SettingCard(icon = Icons.Default.Logout, label = "Cerrar Sesión") {
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        .edit().clear().apply()

                    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .edit().clear().apply()

                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true } // limpia la pila
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null)
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SettingCard(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, fontSize = 16.sp)
        }
    }
}

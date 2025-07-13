package com.example.proyectofinalcarwash.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

data class Cita(
    val fecha: LocalDate,
    val hora: String,
    val duracionMin: Int,
    val servicio: String,
    val vehiculo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val hoy = LocalDate.now()

    // Simulación de citas (luego puedes usar ViewModel o DB)
    val citas = listOf(
        Cita(hoy.plusDays(1), "07:00", 45, "Lavado completo", "Toyota Corolla"),
        Cita(hoy.plusDays(3), "08:40", 95, "Cambio de aceite", "Mazda 3"),
        Cita(hoy.minusDays(2), "10:00", 60, "Pulido de pintura", "Honda Civic")
    )

    val fechasConCitas = citas.map { it.fecha }.distinct()
    val rangoDias = (-2..5).map { hoy.plusDays(it.toLong()) }

    var diaSeleccionado by remember { mutableStateOf(hoy) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Calendario de Citas") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
        ) {
            // Encabezado de días de la semana
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                rangoDias.forEach { fecha ->
                    val seleccionado = fecha == diaSeleccionado
                    Column(
                        modifier = Modifier
                            .clickable { diaSeleccionado = fecha }
                            .background(
                                if (seleccionado) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(text = fecha.dayOfMonth.toString())
                    }
                }
            }

            val citasFiltradas = citas.filter { it.fecha == diaSeleccionado }

            if (citasFiltradas.isEmpty()) {
                Text("No hay citas para este día", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(citasFiltradas) { cita ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("🕒 ${cita.hora} — ${cita.duracionMin} min", fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text("🧽 Servicio: ${cita.servicio}")
                                Text("🚗 Vehículo: ${cita.vehiculo}")
                            }
                        }
                    }
                }
            }
        }
    }
}
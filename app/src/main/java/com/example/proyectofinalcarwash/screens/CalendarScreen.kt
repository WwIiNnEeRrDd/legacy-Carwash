package com.example.proyectofinalcarwash.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.data.model.toVisual
import com.example.proyectofinalcarwash.viewmodel.CitasViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    citasViewModel: CitasViewModel = viewModel()
) {
    val hoy = LocalDate.now()
    var diaSeleccionado by remember { mutableStateOf(hoy) }

    val citas by citasViewModel.citas.collectAsState()

    val citasVisuales = remember(citas) {
        runCatching {
            citas.map { it.toVisual() }
        }.getOrDefault(emptyList())
    }
    val citasFiltradas = citasVisuales.filter { it.fecha == diaSeleccionado }

    val rangoDias = (-10..10).map { hoy.plusDays(it.toLong()) }
    val indexHoy = rangoDias.indexOf(hoy)
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        citasViewModel.fetchCitas()
        listState.scrollToItem(indexHoy)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Calendario de Citas") })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rangoDias) { fecha ->
                    val seleccionado = fecha == diaSeleccionado
                    val esHoy = fecha == hoy

                    Column(
                        modifier = Modifier
                            .clickable { diaSeleccionado = fecha }
                            .then(
                                if (esHoy) Modifier
                                    .background(Color.Transparent, shape = MaterialTheme.shapes.small)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                                else Modifier
                            )
                            .background(
                                if (seleccionado) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (esHoy) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                        Text(
                            text = fecha.dayOfMonth.toString(),
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                            color = if (esHoy) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
            }

            if (citasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay citas para este día", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(citasFiltradas) { cita ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        "detalleCita/${cita.fecha}/${cita.hora}/${cita.servicio}/${cita.vehiculo}/${cita.duracionMin}"
                                    )
                                },
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

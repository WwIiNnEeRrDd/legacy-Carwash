package com.example.proyectofinalcarwash.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectofinalcarwash.viewmodel.ServiciosViewModel
import com.example.proyectofinalcarwash.viewmodel.VehiculosViewModel
import com.example.proyectofinalcarwash.viewmodel.CitasViewModel
import com.example.proyectofinalcarwash.data.model.CrearCitaRequest
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    serviciosViewModel: ServiciosViewModel = viewModel(),
    citasViewModel: CitasViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val vehiculos by vehiculosViewModel.vehiculos.collectAsState()
    val servicios by serviciosViewModel.servicios.collectAsState()

    var vehiculoSeleccionado by remember { mutableStateOf<String?>(null) }
    var servicioSeleccionado by remember { mutableStateOf<String?>(null) }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        vehiculosViewModel.fetchVehiculos()
        serviciosViewModel.fetchServicios()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Agendar nueva cita") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomDropdownMenu(
                label = "Selecciona un vehículo",
                options = vehiculos.map { "${it.id} - ${it.placa}" },
                selectedOption = vehiculoSeleccionado,
                onOptionSelected = { vehiculoSeleccionado = it }
            )

            CustomDropdownMenu(
                label = "Selecciona un servicio",
                options = servicios.map { "${it.id_servicio} - ${it.nombre_servicio}" },
                selectedOption = servicioSeleccionado,
                onOptionSelected = { servicioSeleccionado = it }
            )

            DateSelector("Selecciona una fecha", fecha) { fecha = it }

            TimeSelector("Selecciona una hora", hora) { hora = it }

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val idVehiculo = vehiculoSeleccionado?.split(" - ")?.get(0)?.toIntOrNull()
                    val idServicio = servicioSeleccionado?.split(" - ")?.get(0)?.toIntOrNull()

                    if (idVehiculo == null || idServicio == null || fecha.isBlank() || hora.isBlank()) {
                        Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val request = CrearCitaRequest(
                        id_vehiculo = idVehiculo,
                        id_servicio = idServicio,
                        fecha_cita = fecha,
                        hora_cita = hora,
                        comentario_cliente = comentario
                    )

                    coroutineScope.launch {
                        citasViewModel.crearCita(
                            request,
                            onSuccess = {
                                Toast.makeText(context, "Cita creada correctamente", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DateSelector(label: String, date: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        onDateSelected("%04d-%02d-%02d".format(year, month + 1, day))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                // Esta línea evita fechas pasadas
                datePickerDialog.datePicker.minDate = System.currentTimeMillis()

                datePickerDialog.show()
            }
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TimeSelector(label: String, time: String, onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        // Verificar si la fecha es hoy
                        val selectedDate = calendar.clone() as Calendar
                        selectedDate.set(Calendar.HOUR_OF_DAY, 0)
                        selectedDate.set(Calendar.MINUTE, 0)
                        selectedDate.set(Calendar.SECOND, 0)
                        selectedDate.set(Calendar.MILLISECOND, 0)

                        val today = Calendar.getInstance()
                        today.set(Calendar.HOUR_OF_DAY, 0)
                        today.set(Calendar.MINUTE, 0)
                        today.set(Calendar.SECOND, 0)
                        today.set(Calendar.MILLISECOND, 0)

                        val isToday = selectedDate.timeInMillis == today.timeInMillis

                        if (isToday) {
                            // Si es hoy, validar que la hora no sea pasada
                            if (hourOfDay < currentHour || (hourOfDay == currentHour && minute < currentMinute)) {
                                Toast.makeText(context, "No puedes seleccionar una hora pasada", Toast.LENGTH_SHORT).show()
                                return@TimePickerDialog
                            }
                        }

                        onTimeSelected("%02d:%02d".format(hourOfDay, minute))
                    },
                    currentHour,
                    currentMinute,
                    true
                ).show()
            }
    ) {
        OutlinedTextField(
            value = time,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            trailingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

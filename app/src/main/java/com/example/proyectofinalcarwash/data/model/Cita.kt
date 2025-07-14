package com.example.proyectofinalcarwash.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Modelo que representa la cita tal como la recibes del backend
data class Cita(
    val id_cita: Int,
    val fecha_cita: String,
    val hora_cita: String,
    val estado: String,
    val comentario_cliente: String,
    val fecha_creacion: String,
    val placa: String,
    val nombre_servicio: String,
    val duracion_estimada: Int
)

// Modelo que usas al momento de crear una nueva cita
data class CrearCitaRequest(
    val id_servicio: Int,
    val id_vehiculo: Int,
    val fecha_cita: String,   // Formato: yyyy-MM-dd
    val hora_cita: String,    // Formato: HH:mm
    val comentario_cliente: String = ""
)

// Modelo adaptado para la vista CalendarScreen
data class CitaVisual(
    val fecha: LocalDate,
    val hora: String,
    val duracionMin: Int,
    val servicio: String,
    val vehiculo: String,
)

// Función de extensión que convierte un Cita del backend a CitaVisual
fun Cita.toVisual(): CitaVisual {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME // para formato con Z al final
    val fechaLocalDate = LocalDateTime.parse(this.fecha_cita, formatter).toLocalDate()

    return CitaVisual(
        fecha = fechaLocalDate,
        hora = this.hora_cita.take(5), // "10:00:00" → "10:00"
        duracionMin = this.duracion_estimada,
        servicio = this.nombre_servicio,
        vehiculo = this.placa
    )
}

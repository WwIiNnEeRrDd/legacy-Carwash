package com.example.proyectofinalcarwash.data.model

data class Promocion(
    val id_promocion: Int,
    val nombre_promocion: String,
    val descripcion: String?,
    val descuento_porcentaje: Double,
    val nombre_servicio: String
)

package com.example.proyectofinalcarwash.data.model

data class Vehiculo(
    val id: Int,
    val marca: String,
    val modelo: String,
    val placa: String
)

data class CrearVehiculoResponse(
    val mensaje: String,
    val vehiculo: Vehiculo
)

data class VehiculoRequest(
    val placa: String,
    val marca: String,
    val modelo: String,
    val color: String
)

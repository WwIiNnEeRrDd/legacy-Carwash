package com.example.proyectofinalcarwash.data.model
import com.google.gson.annotations.SerializedName

data class Vehiculo(
    @SerializedName("id_vehiculo") val id: Int,
    val placa: String,
    val marca: String,
    val modelo: String,
    val color: String
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



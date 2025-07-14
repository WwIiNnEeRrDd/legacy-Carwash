package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/register")
    suspend fun registerCliente(@Body request: ClienteRegisterRequest): AuthResponse

    @POST("/api/auth/login")
    suspend fun loginCliente(@Body request: ClienteLoginRequest): AuthResponse

    @GET("/api/servicios")
    suspend fun getServicios(@Header("Authorization") token: String): List<Servicio>

    @GET("/api/vehiculos/mis-vehiculos")
    suspend fun getMisVehiculos(@Header("Authorization") token: String): Response<List<Vehiculo>>

    @PUT("/api/cliente/actualizar")
    suspend fun actualizarPerfil(@Body cliente: Cliente): Response<ActualizarPerfilResponse>

    @POST("/api/vehiculos/crear")
    suspend fun crearVehiculo(
        @Header("Authorization") token: String,
        @Body vehiculo: VehiculoRequest
    ): Response<CrearVehiculoResponse>
}

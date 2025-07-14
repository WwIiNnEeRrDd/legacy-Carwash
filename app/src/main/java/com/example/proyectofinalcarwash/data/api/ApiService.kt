package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteLoginRequest
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import com.example.proyectofinalcarwash.data.model.Servicio
import com.example.proyectofinalcarwash.data.model.Vehiculo
import com.example.proyectofinalcarwash.data.model.Cliente
import com.example.proyectofinalcarwash.data.model.ActualizarPerfilResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

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

}

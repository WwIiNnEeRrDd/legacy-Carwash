package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteLoginRequest
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import com.example.proyectofinalcarwash.data.model.Servicio
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    suspend fun registerCliente(@Body request: ClienteRegisterRequest): AuthResponse

    @POST("/api/auth/login")
    suspend fun loginCliente(@Body request: ClienteLoginRequest): AuthResponse

    @GET("/api/servicios")
    suspend fun getServicios(@Header("Authorization") token: String): List<Servicio>
}

package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ✅ No requiere token
    @POST("/api/auth/register")
    suspend fun registerCliente(@Body request: ClienteRegisterRequest): AuthResponse

    // ✅ No requiere token
    @POST("/api/auth/login")
    suspend fun loginCliente(@Body request: ClienteLoginRequest): AuthResponse

    // ✅ Token inyectado por AuthInterceptor
    @GET("/api/servicios")
    suspend fun getServicios(): List<Servicio>

    // ✅ Token inyectado por AuthInterceptor
    @GET("/api/vehiculos/mis-vehiculos")
    suspend fun getMisVehiculos(): Response<List<Vehiculo>>

    // ✅ Token inyectado por AuthInterceptor
    @PUT("/api/cliente/actualizar")
    suspend fun actualizarPerfil(@Body cliente: Cliente): Response<ActualizarPerfilResponse>

    // ✅ Token inyectado por AuthInterceptor
    @POST("/api/vehiculos/crear")
    suspend fun crearVehiculo(@Body vehiculo: VehiculoRequest): Response<CrearVehiculoResponse>

    // ✅ Token inyectado por AuthInterceptor
    @GET("/api/citas/mis-citas")
    suspend fun getCitas(): Response<List<Cita>>

    // ✅ Token inyectado por AuthInterceptor
    @POST("/api/citas/agendar")
    suspend fun crearCita(@Body cita: CrearCitaRequest): Response<Unit>

    @GET("/api/citas/proxima")
    suspend fun getProximaCita(): Response<Cita>
}

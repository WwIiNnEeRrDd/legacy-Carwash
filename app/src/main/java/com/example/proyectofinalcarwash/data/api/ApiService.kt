package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/register")
    fun registerCliente(@Body request: ClienteRegisterRequest): Call<AuthResponse>
}

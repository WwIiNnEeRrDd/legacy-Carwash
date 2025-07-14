package com.example.proyectofinalcarwash.data.api

import android.content.Context
import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest

object AuthRepository {

    suspend fun registerUser(
        context: Context,
        nombre: String,
        email: String,
        contraseña: String,
        telefono: String
    ): Result<AuthResponse> {
        return try {
            val api = RetrofitClient.create(context)
            val request = ClienteRegisterRequest(nombre, email, contraseña, telefono)
            val response = api.registerCliente(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

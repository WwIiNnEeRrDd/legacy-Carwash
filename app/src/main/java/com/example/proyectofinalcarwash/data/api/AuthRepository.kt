package com.example.proyectofinalcarwash.data.api

import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthRepository {

    fun registerUser(
        nombre: String,
        email: String,
        contraseña: String,
        telefono: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ClienteRegisterRequest(nombre, email, contraseña, telefono)

        RetrofitClient.api.registerCliente(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onError("Error de red: ${t.localizedMessage}")
            }
        })
    }
}

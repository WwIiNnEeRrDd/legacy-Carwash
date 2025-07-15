package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val _registerState = MutableStateFlow<Result<AuthResponse>?>(null)
    val registerState: StateFlow<Result<AuthResponse>?> = _registerState

    fun register(nombre: String, email: String, contraseña: String) {
        val request = ClienteRegisterRequest(nombre, email, contraseña)

        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.registerCliente(request)

                guardarTokenYDatos(response)

                _registerState.value = Result.Success(response)
            } catch (e: HttpException) {
                val message = when (e.code()) {
                    400 -> "Solicitud inválida. Verifica los datos ingresados."
                    409 -> "Este correo ya está registrado. Intenta con otro."
                    else -> "Error del servidor (${e.code()}): ${e.message()}"
                }
                _registerState.value = Result.Failure(Throwable(message))
            }
        }
    }

    private fun guardarTokenYDatos(response: AuthResponse) {
        val app = getApplication<Application>()

        // Guardar token
        app.getSharedPreferences("auth", Context.MODE_PRIVATE).edit().apply {
            putString("token", response.token)
            apply()
        }

        // Guardar datos del cliente
        val cliente = response.cliente
        app.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().apply {
            putString("nombre", cliente.nombre)
            putString("email", cliente.email)
            putString("telefono", cliente.telefono)
            putString("residencia", cliente.residencia)
            apply()
        }
    }
}

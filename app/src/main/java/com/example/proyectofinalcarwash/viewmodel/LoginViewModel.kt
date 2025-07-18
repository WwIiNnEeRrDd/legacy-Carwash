package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteLoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val data: AuthResponse) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    fun login(email: String, contraseña: String) {
        _loginState.value = LoginResult.Loading
        val request = ClienteLoginRequest(email, contraseña)

        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.loginCliente(request)

                guardarTokenYDatos(response)

                _loginState.value = LoginResult.Success(response)
            } catch (e: HttpException) {
                val message = when (e.code()) {
                    400 -> "Solicitud incorrecta. Verifica los datos ingresados."
                    401 -> "Credenciales incorrectas. Intenta de nuevo."
                    403 -> "Acceso denegado. No tienes permisos para ingresar."
                    404 -> "Usuario no encontrado. Verifica tu correo o regístrate."
                    else -> "Error del servidor (${e.code()}): ${e.message()}"
                }
                _loginState.value = LoginResult.Error(message)
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

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
                saveToken(response.token)
                _loginState.value = LoginResult.Success(response)
            } catch (e: HttpException) {
                _loginState.value = LoginResult.Error("Error del servidor: ${e.message}")
            } catch (e: IOException) {
                _loginState.value = LoginResult.Error("No se pudo conectar al servidor")
            } catch (e: Exception) {
                _loginState.value = LoginResult.Error("Error desconocido: ${e.message}")
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("token", token).apply()
    }
}

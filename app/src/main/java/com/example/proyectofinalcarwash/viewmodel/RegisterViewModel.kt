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

    fun register(nombre: String, email: String, contraseña: String, telefono: String) {
        val request = ClienteRegisterRequest(nombre, email, contraseña)

        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.registerCliente(request) // llamada suspend
                saveToken(response.token)
                _registerState.value = Result.Success(response)
            } catch (e: HttpException) {
                _registerState.value = Result.Failure(Throwable("Error HTTP: ${e.message()}"))
            } catch (e: IOException) {
                _registerState.value = Result.Failure(Throwable("No se pudo conectar al servidor"))
            } catch (e: Exception) {
                _registerState.value = Result.Failure(e)
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("token", token).apply()
    }
}

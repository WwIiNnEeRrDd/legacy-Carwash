package com.example.proyectofinalcarwash.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteRegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
    }

    private val _registerState = MutableStateFlow<Result<AuthResponse>?>(null)
    val registerState: StateFlow<Result<AuthResponse>?> = _registerState

    fun register(nombre: String, email: String, contraseña: String) {
        val request = ClienteRegisterRequest(nombre, email, contraseña, "")

        RetrofitClient.api.registerCliente(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = Result.Success(response.body()!!)
                } else {
                    _registerState.value = Result.Failure(Throwable("Error ${response.code()}: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _registerState.value = Result.Failure(t)
            }
        })
    }
}
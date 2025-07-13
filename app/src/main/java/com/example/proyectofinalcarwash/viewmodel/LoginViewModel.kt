package com.example.proyectofinalcarwash.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.AuthResponse
import com.example.proyectofinalcarwash.data.model.ClienteLoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    data class Success(val data: AuthResponse) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState

    fun login(email: String, contraseña: String) {
        _loginState.value = LoginResult.Loading
        val request = ClienteLoginRequest(email, contraseña)

        RetrofitClient.api.loginCliente(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _loginState.value = LoginResult.Success(response.body()!!)
                } else {
                    _loginState.value = LoginResult.Error("Credenciales inválidas")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _loginState.value = LoginResult.Error("Error de red: ${t.localizedMessage}")
            }
        })
    }
}

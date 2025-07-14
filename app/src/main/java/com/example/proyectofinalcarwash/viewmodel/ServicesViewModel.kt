package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Servicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ServiciosViewModel(application: Application) : AndroidViewModel(application) {

    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchServicios() {
        viewModelScope.launch {
            try {
                val token = getTokenFromPrefs()
                val api = RetrofitClient.create(getApplication())
                val serviciosObtenidos = api.getServicios("Bearer $token")
                _servicios.value = serviciosObtenidos
            } catch (e: IOException) {
                _error.value = "No se pudo conectar al servidor"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    private fun getTokenFromPrefs(): String {
        val prefs = getApplication<Application>()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("token", "") ?: ""
    }
}

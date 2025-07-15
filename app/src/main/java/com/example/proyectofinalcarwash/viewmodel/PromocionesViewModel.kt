package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Promocion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class PromocionesViewModel(application: Application) : AndroidViewModel(application) {

    private val _promociones = MutableStateFlow<List<Promocion>>(emptyList())
    val promociones: StateFlow<List<Promocion>> = _promociones

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPromociones() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.getPromociones()
                if (response.isSuccessful) {
                    _promociones.value = response.body().orEmpty()
                } else {
                    _error.value = "Error del servidor: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _error.value = "No se pudo conectar al servidor"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun fetchPromocionesPorServicio(idServicio: Int) {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.getPromocionesPorServicio(idServicio)
                _promociones.value = response.body() ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Vehiculo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class VehiculosViewModel(application: Application) : AndroidViewModel(application) {

    private val _vehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculo>> = _vehiculos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchVehiculos()
    }

    fun fetchVehiculos() {
        viewModelScope.launch {
            try {
                val token = getTokenFromPrefs()
                val api = RetrofitClient.create(getApplication())
                val response = api.getMisVehiculos("Bearer $token")

                if (response.isSuccessful) {
                    _vehiculos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: IOException) {
                _error.value = "No se pudo conectar al servidor"
            } catch (e: HttpException) {
                _error.value = "Error HTTP: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Error desconocido: ${e.message}"
            }
        }
    }

    private fun getTokenFromPrefs(): String {
        val prefs = getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("token", "") ?: ""
    }
}

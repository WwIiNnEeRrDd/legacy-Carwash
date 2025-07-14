package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Cita
import com.example.proyectofinalcarwash.data.model.CrearCitaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CitasViewModel(application: Application) : AndroidViewModel(application) {

    private val _citas = MutableStateFlow<List<Cita>>(emptyList())
    val citas: StateFlow<List<Cita>> get() = _citas

    private val _proximaCita = MutableStateFlow<Cita?>(null)
    val proximaCita: StateFlow<Cita?> get() = _proximaCita

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchCitas() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.getCitas()
                if (response.isSuccessful) {
                    _citas.value = response.body().orEmpty()
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

    fun crearCita(
        request: CrearCitaRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.crearCita(request)
                if (response.isSuccessful) {
                    fetchCitas() // Actualizar lista de citas
                    fetchProximaCita() // Actualizar la próxima también
                    onSuccess()
                } else {
                    onError("Error del servidor: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                onError("No se pudo conectar al servidor")
            } catch (e: Exception) {
                onError("Error desconocido: ${e.message}")
            }
        }
    }

    fun fetchProximaCita() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.getProximaCita()
                _proximaCita.value = if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                _proximaCita.value = null
            }
        }
    }
}

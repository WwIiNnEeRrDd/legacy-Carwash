package com.example.proyectofinalcarwash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalcarwash.data.api.RetrofitClient
import com.example.proyectofinalcarwash.data.model.Vehiculo
import com.example.proyectofinalcarwash.data.model.VehiculoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import org.json.JSONObject

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
                val api = RetrofitClient.create(getApplication())
                val response = api.getMisVehiculos()

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

    fun crearVehiculo(
        vehiculoRequest: VehiculoRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.crearVehiculo(vehiculoRequest)

                when {
                    response.isSuccessful -> {
                        fetchVehiculos()
                        onSuccess()
                    }
                    response.code() == 409 -> {
                        onError("Ya existe un vehículo con esa placa")
                    }
                    response.code() == 401 -> {
                        onError("Sesión expirada. Vuelve a iniciar sesión.")
                    }
                    else -> {
                        onError("Error del servidor: ${response.code()}")
                    }
                }
            } catch (e: IOException) {
                onError("No se pudo conectar al servidor")
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun eliminarVehiculo(
        idVehiculo: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.create(getApplication())
                val response = api.eliminarVehiculo(idVehiculo)

                if (response.isSuccessful) {
                    fetchVehiculos()
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val mensajeError = try {
                        JSONObject(errorBody).getString("error")
                    } catch (e: Exception) {
                        "Error al eliminar vehículo: ${response.code()} ${response.message()}"
                    }
                    onError(mensajeError)
                }
            } catch (e: IOException) {
                onError("No se pudo conectar al servidor.")
            } catch (e: Exception) {
                onError("Error inesperado: ${e.message}")
            }
        }
    }

}

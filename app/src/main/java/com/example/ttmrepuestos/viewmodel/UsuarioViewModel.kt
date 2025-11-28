package com.example.ttmrepuestos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.repository.UsuarioRepository
import com.example.ttmrepuestos.remote.LoginResponse
import com.example.ttmrepuestos.remote.RegistroResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

// Sealed class para representar los estados de la UI (Cargando, Éxito, Error)
sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
    object Idle : AuthResult<Nothing>() // Estado inicial
}

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // StateFlow para el resultado del login
    private val _loginResult = MutableStateFlow<AuthResult<LoginResponse>>(AuthResult.Idle)
    val loginResult: StateFlow<AuthResult<LoginResponse>> = _loginResult

    // StateFlow para el resultado del registro
    private val _registroResult = MutableStateFlow<AuthResult<RegistroResponse>>(AuthResult.Idle)
    val registroResult: StateFlow<AuthResult<RegistroResponse>> = _registroResult

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _loginResult.value = AuthResult.Loading
            try {
                val response = repository.login(correo, contrasena)
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = AuthResult.Success(response.body()!!)
                } else {
                    // Aquí podrías parsear el JSON de error para un mensaje más específico
                    _loginResult.value = AuthResult.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error(e.message ?: "Ocurrió un error desconocido")
            }
        }
    }

    fun registrar(usuario: Usuario) {
        viewModelScope.launch {
            _registroResult.value = AuthResult.Loading
            try {
                val response = repository.registrar(usuario)
                if (response.isSuccessful && response.body() != null) {
                    _registroResult.value = AuthResult.Success(response.body()!!)
                } else {
                    _registroResult.value = AuthResult.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _registroResult.value = AuthResult.Error(e.message ?: "Ocurrió un error desconocido")
            }
        }
    }
}

package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.remote.ApiService
import com.example.ttmrepuestos.remote.LoginResponse
import com.example.ttmrepuestos.remote.RegistroResponse
import retrofit2.Response

class UsuarioRepository(private val apiService: ApiService) {

    suspend fun login(correo: String, contrasena: String): Response<LoginResponse> {
        val loginInfo = mapOf("correo" to correo, "contrasena" to contrasena)
        return apiService.login(loginInfo)
    }

    suspend fun registrar(usuario: Usuario): Response<RegistroResponse> {
        return apiService.registrarUsuario(usuario)
    }
}

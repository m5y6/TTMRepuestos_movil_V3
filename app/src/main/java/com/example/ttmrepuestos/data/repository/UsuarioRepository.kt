package com.example.ttmrepuestos.data.repository

import android.util.Log
import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.data.local.UsuarioDao
import com.example.ttmrepuestos.remote.UsuarioApiService

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val usuarioApiService: UsuarioApiService
) {

    suspend fun refreshUsers() {
        try {
            val remoteUsers = usuarioApiService.getUsers()
            // Opcional: podrías querer borrar los usuarios locales antes de insertar los nuevos
            // para mantener la consistencia con la fuente remota.
            usuarioDao.deleteAllUsers() // Necesitarás añadir este método en tu UsuarioDao
            remoteUsers.forEach {
                usuarioDao.insertUser(it)
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error refreshing users", e)
        }
    }

    suspend fun registrarUsuario(usuario: Usuario) {
        // El registro ahora solo funcionará a nivel local,
        // ya que la API del Gist no permite enviar nuevos usuarios.
        usuarioDao.insertUser(usuario)
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        // La lógica de inicio de sesión no cambia,
        // pero ahora operará sobre los datos sincronizados desde la API.
        val usuario = usuarioDao.getUserByEmail(correo)
        if (usuario != null && usuario.contrasena == contrasena) {
            return usuario
        }
        return null
    }
}

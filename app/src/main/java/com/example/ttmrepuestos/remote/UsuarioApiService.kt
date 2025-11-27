package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.Usuario
import retrofit2.http.GET

interface UsuarioApiService {
    @GET("92d72a42981026ab5b6807807f7084d94bfe727e/gistfile1.txt")
    suspend fun getUsers(): List<Usuario>
}

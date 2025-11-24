package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.model.Producto
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    @GET("movilTTM") // Se ajusta al nombre del archivo en el Gist
    suspend fun getProducts(): List<Producto>
}
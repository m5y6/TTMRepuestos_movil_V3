package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.Producto
import retrofit2.http.GET

interface ApiService {
    @GET("api/productos")
    suspend fun getProducts(): List<Producto>
}

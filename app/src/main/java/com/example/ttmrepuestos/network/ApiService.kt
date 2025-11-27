package com.example.ttmrepuestos.network

import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.network.response.TestResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/test")
    suspend fun getTestMessage(): TestResponse

    @GET("api/productos")
    suspend fun getProductos(): List<Producto>
}

package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.model.Usuario
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- PRODUCTOS ---
    @GET("/api/productos")
    suspend fun getProducts(): List<Producto>

    @POST("/api/productos")
    suspend fun addProduct(@Body producto: Producto): Response<ApiResponse>

    @PUT("/api/productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: Producto): Response<ApiResponse>

    @DELETE("/api/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse>

    // --- USUARIOS ---
    @POST("/api/usuarios/login")
    suspend fun login(@Body loginInfo: Map<String, String>): Response<LoginResponse>

    @POST("/api/usuarios/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<RegistroResponse>

    // --- IMÁGENES (NUEVO) ---
    @Multipart
    @POST("/api/upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUploadResponse>
}

// --- CLASES DE RESPUESTA ---

data class LoginResponse(val message: String, val usuario: Usuario)
data class RegistroResponse(val message: String, val userId: Int)
data class ApiResponse(val message: String)
// Clase para la respuesta de la subida de imagen (NUEVO)
data class ImageUploadResponse(val imageUrl: String)

package com.example.ttmrepuestos.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.ttmrepuestos.data.local.ProductoDao
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.remote.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductoRepository(
    private val dao: ProductoDao,
    private val apiService: ApiService
) {
    val products: Flow<List<Producto>> = dao.getAllProducts()

    suspend fun refreshProducts() {
        try {
            val remoteProducts = apiService.getProducts()
            dao.deleteAllProducts()
            remoteProducts.forEach { producto ->
                dao.insertProduct(producto)
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error refreshing products", e)
        }
    }

    // --- NUEVA FUNCIÓN PARA SUBIR IMAGEN ---
    suspend fun uploadImage(context: Context, bitmap: Bitmap): String? {
        return try {
            val file = File(context.cacheDir, "temp_image.jpeg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = apiService.uploadImage(body)
            file.delete()

            if (response.isSuccessful) {
                response.body()?.imageUrl
            } else {
                Log.e("ProductoRepository", "Error uploading image: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Exception uploading image", e)
            null
        }
    }

    suspend fun addProduct(producto: Producto) {
        try {
            val response = apiService.addProduct(producto)
            if (response.isSuccessful) {
                refreshProducts()
            } else {
                Log.e("ProductoRepository", "Error adding product: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Exception adding product", e)
        }
    }

    suspend fun updateProduct(producto: Producto) {
        try {
            val response = apiService.updateProduct(producto.id, producto)
            if (response.isSuccessful) {
                refreshProducts()
            } else {
                Log.e("ProductoRepository", "Error updating product: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Exception updating product", e)
        }
    }

    suspend fun deleteProduct(producto: Producto) {
        try {
            val response = apiService.deleteProduct(producto.id)
            if (response.isSuccessful) {
                refreshProducts()
            } else {
                Log.e("ProductoRepository", "Error deleting product: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Exception deleting product", e)
        }
    }
}

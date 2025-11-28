package com.example.ttmrepuestos.data.repository

import android.util.Log
import com.example.ttmrepuestos.data.local.ProductoDao
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.network.ApiService // Import corregido
import kotlinx.coroutines.flow.Flow

class ProductoRepository(
    private val dao: ProductoDao,
    private val apiService: ApiService // Inyectar ApiService
) {
    val products: Flow<List<Producto>> = dao.getAllProducts()

    suspend fun refreshProducts() {
        try {
            // Llamada corregida
            val remoteProducts = apiService.getProductos()
            dao.deleteAllProducts()
            remoteProducts.forEach {
                dao.insertProduct(it)
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error refreshing products", e)
        }
    }

    suspend fun insert(producto: Producto): Long = dao.insertProduct(producto)

    suspend fun update(producto: Producto) = dao.updateProduct(producto)

    suspend fun delete(producto: Producto) = dao.deleteProduct(producto)
}

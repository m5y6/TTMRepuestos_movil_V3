package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.data.local.ProductoDao
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.remote.ApiService
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class ProductoRepositoryTest : StringSpec({

    "refreshProducts debe obtener productos de la API y guardarlos en la base de datos local" {
        // 1. Arrange: Configuración inicial
        val fakeProductos = listOf(
            Producto(1, "Producto 1", 100, "Descripción 1", "Categoría 1", null),
            Producto(2, "Producto 2", 200, "Descripción 2", "Categoría 2", null)
        )

        val mockApiService = mockk<ApiService>()
        val mockProductoDao = mockk<ProductoDao>(relaxed = true) // relaxed = true para no tener que simular todas las llamadas

        // Simular la respuesta de la API
        coEvery { mockApiService.getProducts() } returns fakeProductos

        val repository = ProductoRepository(mockProductoDao, mockApiService)

        // 2. Act: Ejecutar la acción que se quiere probar
        runTest {
            repository.refreshProducts()
        }

        // 3. Assert: Verificar los resultados
        coVerify {
            // Verificar que se llamó a la API para obtener los productos
            mockApiService.getProducts()

            // Verificar que se borraron los productos antiguos
            mockProductoDao.deleteAllProducts()

            // Verificar que cada producto nuevo fue insertado
            fakeProductos.forEach { producto ->
                mockProductoDao.insertProduct(producto)
            }
        }
    }
})

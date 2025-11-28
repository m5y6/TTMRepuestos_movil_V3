package com.example.ttmrepuestos.repository

import android.util.Log
import com.example.ttmrepuestos.data.local.ProductoDao
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.model.Producto
import com.example.ttmrepuestos.remote.ApiService // <-- CORRECCIÓN 1: Importar desde 'remote'
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class ProductoRepositoryTest : StringSpec({

    lateinit var mockApi: ApiService
    lateinit var mockDao: ProductoDao
    lateinit var repository: ProductoRepository

    beforeTest {
        mockApi = mockk()
        mockDao = mockk(relaxed = true) // Relaxed para no tener que mockear todas las llamadas
        repository = ProductoRepository(mockDao, mockApi)

        mockkStatic(Log::class)
        every { Log.e(any(), any<String>(), any()) } returns 0
    }

    afterTest {
        unmockkStatic(Log::class)
    }

    "el flow de products debe emitir los datos del DAO" {
        runTest {
            val productsFromDao = listOf(Producto(id = 1, nombre = "Producto 1", precio = 100, descripcion = "Desc 1", categoria = "Cat 1"))
            every { mockDao.getAllProducts() } returns flowOf(productsFromDao)
            
            // Recreamos el repo para que tome el nuevo flow
            val newRepository = ProductoRepository(mockDao, mockApi)
            
            val result = newRepository.products.first()
            result shouldBe productsFromDao
        }
    }

    "refreshProducts debe obtener datos de la API y guardarlos en el DAO" {
        runTest {
            val productsFromApi = listOf(Producto(id = 1, nombre = "Producto API", precio = 150, descripcion = "Desc API", categoria = "Cat API"))
            // CORRECCIÓN 2: Usar 'getProducts'
            coEvery { mockApi.getProducts() } returns productsFromApi
            coEvery { mockDao.deleteAllProducts() } returns Unit
            coEvery { mockDao.insertProduct(any()) } returns 1L

            repository.refreshProducts()

            coVerify(exactly = 1) { mockApi.getProducts() }
            coVerify(exactly = 1) { mockDao.deleteAllProducts() }
            productsFromApi.forEach {
                coVerify { mockDao.insertProduct(it) }
            }
        }
    }

    "si la API falla, refreshProducts debe llamar a Log.e y no interactuar con el DAO" {
        runTest {
            val apiException = RuntimeException("Error de red")
            // CORRECCIÓN 3: Usar 'getProducts'
            coEvery { mockApi.getProducts() } throws apiException

            repository.refreshProducts()

            coVerify(exactly = 1) { Log.e("ProductoRepository", "Error refreshing products", apiException) }
            coVerify(exactly = 0) { mockDao.deleteAllProducts() }
            coVerify(exactly = 0) { mockDao.insertProduct(any()) }
        }
    }
})

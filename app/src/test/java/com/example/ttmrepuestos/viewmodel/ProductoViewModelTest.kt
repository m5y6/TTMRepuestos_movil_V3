package com.example.ttmrepuestos.viewmodel

import android.app.Application
import com.example.ttmrepuestos.data.repository.ProductoRepository
import com.example.ttmrepuestos.model.Producto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class ProductoViewModelTest : StringSpec({

    lateinit var mockRepository: ProductoRepository
    lateinit var mockApplication: Application
    lateinit var viewModel: ProductoViewModel
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk(relaxed = true)
        mockApplication = mockk(relaxed = true) // Mock para Application

        every { mockRepository.products } returns flowOf(emptyList())
        coEvery { mockRepository.refreshProducts() } returns Unit

        // Pasamos el mock de Application al constructor
        viewModel = ProductoViewModel(mockApplication, mockRepository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "al iniciar, el ViewModel debe llamar a refreshProducts" {
        runTest(testDispatcher) {
            advanceUntilIdle()
            coVerify(exactly = 1) { mockRepository.refreshProducts() }
        }
    }

    "addProduct debe llamar a repository.addProduct con el producto y la foto" {
        runTest(testDispatcher) {
            val producto = Producto(nombre = "Nuevo Prod", precio = 123, descripcion = "Desc", categoria = "Cat")
            
            // Act
            viewModel.addProduct(producto, null) // Pasamos el objeto y la foto
            advanceUntilIdle()

            // Assert
            coVerify { mockRepository.addProduct(producto) }
        }
    }

    "deleteProduct debe llamar a repository.deleteProduct" {
        runTest(testDispatcher) {
            val productoParaBorrar = Producto(id = 1, nombre = "Para Borrar", precio = 100, descripcion = "...", categoria = "...")

            // Act
            viewModel.deleteProduct(productoParaBorrar)
            advanceUntilIdle()

            // Assert
            coVerify { mockRepository.deleteProduct(productoParaBorrar) }
        }
    }

    "updateProduct debe llamar a repository.updateProduct con el producto actualizado" {
        runTest(testDispatcher) {
            val productoActualizado = Producto(id = 1, nombre = "Nuevo Nombre", precio = 150, descripcion = "Nueva Desc", categoria = "Nueva Cat")

            // Act
            viewModel.updateProduct(productoActualizado)
            advanceUntilIdle()

            // Assert
            coVerify { mockRepository.updateProduct(productoActualizado) }
        }
    }

    "el StateFlow de products debe emitir los datos del repositorio" {
        runTest(testDispatcher) {
            val fakeProducts = listOf(Producto(id = 1, nombre = "Prod 1", precio = 100, descripcion = "...", categoria = "..."))
            every { mockRepository.products } returns flowOf(fakeProducts)

            val newViewModel = ProductoViewModel(mockApplication, mockRepository)
            advanceUntilIdle()

            newViewModel.products.value shouldBe fakeProducts
        }
    }
})

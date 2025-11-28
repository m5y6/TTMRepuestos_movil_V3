package com.example.ttmrepuestos.viewmodel

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
    lateinit var viewModel: ProductoViewModel
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk(relaxed = true) // relaxed para no mockear todas las llamadas

        // Mockeamos las llamadas que se hacen en el constructor/init
        every { mockRepository.products } returns flowOf(emptyList())
        coEvery { mockRepository.refreshProducts() } returns Unit

        viewModel = ProductoViewModel(mockRepository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "al iniciar, el ViewModel debe llamar a refreshProducts" {
        runTest(testDispatcher) {
            // Act: Avanzamos el dispatcher para que se ejecute la corrutina del `init`
            advanceUntilIdle()
            // Assert: Verificamos que se llamó a la función de refresco
            coVerify(exactly = 1) { mockRepository.refreshProducts() }
        }
    }

    "addProduct debe llamar a repository.insert con el producto correcto" {
        runTest(testDispatcher) {
            // Arrange
            val producto = Producto(nombre = "Nuevo Prod", precio = 123, descripcion = "Desc", categoria = "Cat")
            coEvery { mockRepository.insert(any()) } returns 1L // Asumimos que devuelve un ID

            // Act
            viewModel.addProduct("Nuevo Prod", 123, "Desc", "Cat", null)
            advanceUntilIdle()

            // Assert: Verificamos que se llamó a insert con un producto con los datos correctos
            coVerify { mockRepository.insert(producto) }
        }
    }

    "deleteProduct debe llamar a repository.delete" {
        runTest(testDispatcher) {
            // Arrange
            val productoParaBorrar = Producto(id = 1, nombre = "Para Borrar", precio = 100, descripcion = "...", categoria = "...")

            // Act
            viewModel.deleteProduct(productoParaBorrar)
            advanceUntilIdle()

            // Assert
            coVerify { mockRepository.delete(productoParaBorrar) }
        }
    }

    "updateProduct debe llamar a repository.update con el producto actualizado" {
        runTest(testDispatcher) {
            // Arrange
            val productoOriginal = Producto(id = 1, nombre = "Original", precio = 100, descripcion = "Original", categoria = "Original")
            val productoActualizado = productoOriginal.copy(nombre = "Nuevo Nombre", precio = 150, descripcion = "Nueva Desc", categoria = "Nueva Cat")

            // Act
            viewModel.updateProduct(productoOriginal, "Nuevo Nombre", 150, "Nueva Desc", "Nueva Cat")
            advanceUntilIdle()

            // Assert
            coVerify { mockRepository.update(productoActualizado) }
        }
    }

    "el StateFlow de products debe emitir los datos del repositorio" {
        runTest(testDispatcher) {
            // Arrange
            val fakeProducts = listOf(Producto(id = 1, nombre = "Prod 1", precio = 100, descripcion = "...", categoria = "..."))
            // Configuramos el mock para que emita la lista falsa
            every { mockRepository.products } returns flowOf(fakeProducts)

            // Act: Creamos una nueva instancia del ViewModel para que recoja el nuevo Flow
            val newViewModel = ProductoViewModel(mockRepository)
            
            // CORRECCIÓN: Damos tiempo a la corrutina de stateIn para ejecutarse
            advanceUntilIdle()

            // Assert: Verificamos que el valor del StateFlow coincide con los datos falsos
            newViewModel.products.value shouldBe fakeProducts
        }
    }
})

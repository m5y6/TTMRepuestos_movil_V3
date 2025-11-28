package com.example.ttmrepuestos.viewmodel

import com.example.ttmrepuestos.data.repository.UsuarioRepository
import com.example.ttmrepuestos.model.Usuario
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class UsuarioViewModelTest : StringSpec({

    lateinit var mockRepository: UsuarioRepository
    lateinit var viewModel: UsuarioViewModel
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk<UsuarioRepository>(relaxed = true)
        viewModel = UsuarioViewModel(mockRepository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "login con credenciales correctas debe actualizar loginResult con exito" {
        runTest(testDispatcher) {
            // Arrange
            val email = "test@test.com"
            val password = "password"
            val usuario = Usuario(1, "Test", "User", 25, email, "123456", password)
            coEvery { mockRepository.iniciarSesion(email, password) } returns usuario

            // Act
            viewModel.login(email, password)
            advanceUntilIdle() // Corregido

            // Assert
            val result = viewModel.loginResult.value
            result?.isSuccess shouldBe true
            result?.getOrNull() shouldBe usuario
        }
    }

    "login con credenciales incorrectas debe actualizar loginResult con fallo" {
        runTest(testDispatcher) {
            // Arrange
            val email = "test@test.com"
            val password = "wrongpassword"
            coEvery { mockRepository.iniciarSesion(email, password) } returns null

            // Act
            viewModel.login(email, password)
            advanceUntilIdle() // Corregido

            // Assert
            val result = viewModel.loginResult.value
            result?.isFailure shouldBe true
            result?.exceptionOrNull()?.message shouldBe "Correo o contraseña incorrectos"
        }
    }

    "register debe llamar a registrarUsuario del repositorio y actualizar registerResult con exito" {
        runTest(testDispatcher) {
            // Arrange
            val nuevoUsuario = Usuario(0, "Nuevo", "Usuario", 28, "nuevo@test.com", "654321", "newpass")
            coEvery { mockRepository.registrarUsuario(nuevoUsuario) } returns Unit

            // Act
            viewModel.register(nuevoUsuario)
            advanceUntilIdle() // Corregido

            // Assert
            coVerify { mockRepository.registrarUsuario(nuevoUsuario) }
            val result = viewModel.registerResult.value
            result?.isSuccess shouldBe true
        }
    }

    "la falla en el registro debe actualizar registerResult con fallo" {
        runTest(testDispatcher) {
            // Arrange
            val nuevoUsuario = Usuario(0, "Nuevo", "Usuario", 28, "nuevo@test.com", "654321", "newpass")
            val exception = Exception("Error en la base de datos")
            coEvery { mockRepository.registrarUsuario(nuevoUsuario) } throws exception

            // Act
            viewModel.register(nuevoUsuario)
            advanceUntilIdle() // Corregido

            // Assert
            val result = viewModel.registerResult.value
            result?.isFailure shouldBe true
            result?.exceptionOrNull() shouldBe exception
        }
    }

    "al iniciar el viewModel debe llamar a refreshUsers" {
        runTest(testDispatcher) {
            advanceUntilIdle() // Corregido
            coVerify { mockRepository.refreshUsers() }
        }
    }

    "login con excepcion en el repositorio debe actualizar loginResult con fallo" {
        runTest(testDispatcher) {
            // Arrange
            val email = "test@test.com"
            val password = "password"
            val exception = RuntimeException("Error de red")
            coEvery { mockRepository.iniciarSesion(email, password) } throws exception

            // Act
            viewModel.login(email, password)
            advanceUntilIdle() // Corregido

            // Assert
            val result = viewModel.loginResult.value
            result?.isFailure shouldBe true
            result?.exceptionOrNull() shouldBe exception
        }
    }
})

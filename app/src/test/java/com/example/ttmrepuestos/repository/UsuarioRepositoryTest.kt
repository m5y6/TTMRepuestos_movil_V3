package com.example.ttmrepuestos.repository

import android.util.Log
import com.example.ttmrepuestos.data.local.UsuarioDao
import com.example.ttmrepuestos.data.repository.UsuarioRepository
import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.remote.UsuarioApiService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest

class UsuarioRepositoryTest : StringSpec({

    lateinit var mockApiService: UsuarioApiService
    lateinit var mockDao: UsuarioDao
    lateinit var repository: UsuarioRepository

    beforeTest {
        mockApiService = mockk()
        mockDao = mockk()
        repository = UsuarioRepository(mockDao, mockApiService)

        // CORRECCIÓN: Mockeamos la clase estática Log de Android
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>(), any()) } returns 0
    }

    afterTest {
        // Limpiamos el mock estático
        unmockkStatic(Log::class)
    }

    "iniciarSesion con credenciales correctas debe devolver el usuario" {
        runTest {
            val email = "test@test.com"
            val password = "password"
            val usuario = Usuario(1, "Test", "User", 25, email, "123456", password)
            coEvery { mockDao.getUserByEmail(email) } returns usuario
            val result = repository.iniciarSesion(email, password)
            result shouldBe usuario
        }
    }

    "registrarUsuario debe llamar a insertUser del DAO" {
        runTest {
            val nuevoUsuario = Usuario(0, "Nuevo", "User", 30, "nuevo@test.com", "654321", "newpass")
            coEvery { mockDao.insertUser(nuevoUsuario) } returns 1L
            repository.registrarUsuario(nuevoUsuario)
            coVerify(exactly = 1) { mockDao.insertUser(nuevoUsuario) }
            coVerify(exactly = 0) { mockApiService.getUsers() }
        }
    }

    "refreshUsers debe obtener usuarios de la API y guardarlos en el DAO" {
        runTest {
            val usersFromApi = listOf(Usuario(1, "Api", "User1", 40, "api1@test.com", "111", "apipass1"))
            coEvery { mockApiService.getUsers() } returns usersFromApi
            coEvery { mockDao.deleteAllUsers() } returns Unit
            coEvery { mockDao.insertUser(any()) } returns 1L
            repository.refreshUsers()
            coVerify(exactly = 1) { mockApiService.getUsers() }
            coVerify(exactly = 1) { mockDao.deleteAllUsers() }
            usersFromApi.forEach {
                coVerify(exactly = 1) { mockDao.insertUser(it) }
            }
        }
    }

    "si la API falla en refreshUsers, debe llamar a Log.e y no interactuar con el DAO" {
        runTest {
            // Arrange
            val apiException = RuntimeException("Error de red")
            coEvery { mockApiService.getUsers() } throws apiException

            // Act
            repository.refreshUsers()

            // Assert
            coVerify(exactly = 1) { Log.e("UsuarioRepository", "Error refreshing users", apiException) }
            coVerify(exactly = 0) { mockDao.deleteAllUsers() }
            coVerify(exactly = 0) { mockDao.insertUser(any()) }
        }
    }
})

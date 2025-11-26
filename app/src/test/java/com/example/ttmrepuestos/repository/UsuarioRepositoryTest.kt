package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.data.local.UsuarioDao
import com.example.ttmrepuestos.data.repository.UsuarioRepository
import com.example.ttmrepuestos.model.Usuario
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class UsuarioRepositoryTest : StringSpec({

    lateinit var mockUsuarioDao: UsuarioDao
    lateinit var repository: UsuarioRepository

    beforeTest {
        mockUsuarioDao = mockk<UsuarioDao>(relaxed = true)
        repository = UsuarioRepository(mockUsuarioDao)
    }

    "registrarUsuario debe llamar a insertUser del DAO" {
        runTest {
            // Arrange
            val nuevoUsuario = Usuario(0, "Juan", "Perez", 30, "juan@test.com", "123456", "password123")

            // Act
            repository.registrarUsuario(nuevoUsuario)

            // Assert
            coVerify { mockUsuarioDao.insertUser(nuevoUsuario) }
        }
    }

    "iniciarSesion con credenciales correctas debe devolver el usuario" {
        runTest {
            // Arrange
            val email = "juan@test.com"
            val password = "password123"
            val usuarioExistente = Usuario(1, "Juan", "Perez", 30, email, "123456", password)
            coEvery { mockUsuarioDao.getUserByEmail(email) } returns usuarioExistente

            // Act
            val result = repository.iniciarSesion(email, password)

            // Assert
            result shouldBe usuarioExistente
        }
    }

    "iniciarSesion con contraseña incorrecta debe devolver null" {
        runTest {
            // Arrange
            val email = "juan@test.com"
            val passwordCorrecta = "password123"
            val passwordIncorrecta = "wrongpassword"
            val usuarioExistente = Usuario(1, "Juan", "Perez", 30, email, "123456", passwordCorrecta)
            coEvery { mockUsuarioDao.getUserByEmail(email) } returns usuarioExistente

            // Act
            val result = repository.iniciarSesion(email, passwordIncorrecta)

            // Assert
            result shouldBe null
        }
    }

    "iniciarSesion con usuario inexistente debe devolver null" {
        runTest {
            // Arrange
            val email = "noexiste@test.com"
            coEvery { mockUsuarioDao.getUserByEmail(email) } returns null

            // Act
            val result = repository.iniciarSesion(email, "password")

            // Assert
            result shouldBe null
        }
    }
})

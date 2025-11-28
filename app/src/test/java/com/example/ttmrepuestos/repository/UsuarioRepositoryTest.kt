package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.remote.ApiService
import com.example.ttmrepuestos.remote.LoginResponse
import com.example.ttmrepuestos.remote.RegistroResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response

class UsuarioRepositoryTest : StringSpec({

    lateinit var mockApiService: ApiService
    lateinit var repository: UsuarioRepository

    beforeTest {
        mockApiService = mockk()
        repository = UsuarioRepository(mockApiService)
    }

    "login debe llamar a apiService.login y devolver la respuesta" {
        runTest {
            val correo = "test@test.com"
            val contrasena = "password"
            val loginInfo = mapOf("correo" to correo, "contrasena" to contrasena)
            val fakeUser = Usuario(1, "Test", "User", 25, correo, "123", contrasena)
            val fakeResponse = Response.success(LoginResponse("OK", fakeUser))

            // Arrange: Cuando se llame a login en la api, devolvemos la respuesta falsa
            coEvery { mockApiService.login(loginInfo) } returns fakeResponse

            // Act: Llamamos a la función del repositorio
            val result = repository.login(correo, contrasena)

            // Assert: Comprobamos que el resultado sea el esperado
            result shouldBe fakeResponse
        }
    }

    "registrar debe llamar a apiService.registrarUsuario y devolver la respuesta" {
        runTest {
            val newUser = Usuario(nombre = "Nuevo", apellido = "User", edad = 30, correo = "nuevo@test.com", telefono = "456", contrasena = "newpass")
            val fakeResponse = Response.success(RegistroResponse("OK", 1))

            // Arrange
            coEvery { mockApiService.registrarUsuario(newUser) } returns fakeResponse

            // Act
            val result = repository.registrar(newUser)

            // Assert
            result shouldBe fakeResponse
        }
    }
})

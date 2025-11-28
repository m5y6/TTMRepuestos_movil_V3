package com.example.ttmrepuestos.viewmodel

import com.example.ttmrepuestos.model.Usuario
import com.example.ttmrepuestos.repository.UsuarioRepository
import com.example.ttmrepuestos.remote.LoginResponse
import com.example.ttmrepuestos.remote.RegistroResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

@ExperimentalCoroutinesApi
class UsuarioViewModelTest : StringSpec({

    lateinit var mockRepository: UsuarioRepository
    lateinit var viewModel: UsuarioViewModel
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        viewModel = UsuarioViewModel(mockRepository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "login con credenciales correctas debe actualizar loginResult a Success" {
        runTest(testDispatcher) {
            val email = "test@test.com"
            val password = "password"
            val usuario = Usuario(1, "Test", "User", 25, email, "123456", password)
            val successResponse = Response.success(LoginResponse("OK", usuario))
            coEvery { mockRepository.login(email, password) } returns successResponse

            viewModel.login(email, password)
            advanceUntilIdle()

            val result = viewModel.loginResult.value
            result.shouldBeInstanceOf<AuthResult.Success<LoginResponse>>()
            (result as AuthResult.Success).data.usuario shouldBe usuario
        }
    }

    "login con credenciales incorrectas debe actualizar loginResult a Error" {
        runTest(testDispatcher) {
            val email = "test@test.com"
            val password = "wrong"
            val errorResponse = Response.error<LoginResponse>(401, "".toResponseBody(null))
            coEvery { mockRepository.login(email, password) } returns errorResponse

            viewModel.login(email, password)
            advanceUntilIdle()

            viewModel.loginResult.value.shouldBeInstanceOf<AuthResult.Error>()
        }
    }

    "registrar debe llamar al repositorio y actualizar registroResult a Success" {
        runTest(testDispatcher) {
            val newUser = Usuario(nombre = "Nuevo", apellido = "User", edad = 30, correo = "nuevo@test.com", telefono = "456", contrasena = "newpass")
            val successResponse = Response.success(RegistroResponse("Created", 1))
            coEvery { mockRepository.registrar(newUser) } returns successResponse

            viewModel.registrar(newUser)
            advanceUntilIdle()

            val result = viewModel.registroResult.value
            result.shouldBeInstanceOf<AuthResult.Success<RegistroResponse>>()
            (result as AuthResult.Success).data.userId shouldBe 1
        }
    }
})

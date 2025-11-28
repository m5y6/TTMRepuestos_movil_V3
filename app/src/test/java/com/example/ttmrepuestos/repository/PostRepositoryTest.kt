package com.example.ttmrepuestos.repository

import android.util.Log
import com.example.ttmrepuestos.data.local.PostDao
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.network.ApiService
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

class PostRepositoryTest : StringSpec({

    lateinit var mockApi: ApiService
    lateinit var mockDao: PostDao
    lateinit var repository: PostRepository

    beforeTest {
        mockApi = mockk()
        mockDao = mockk()
        every { mockDao.getAllPosts() } returns flowOf(emptyList())
        repository = PostRepository(mockDao, mockApi)

        // CORRECCIÓN: Mockeamos la clase estática Log de Android
        mockkStatic(Log::class)
        // Le decimos que cuando se llame a Log.e(any(), any(), any()), no haga nada y devuelva 0
        every { Log.e(any(), any<String>(), any()) } returns 0
    }

    afterTest {
        // Limpiamos el mock estático después del test
        unmockkStatic(Log::class)
    }

    "el flow de posts debe emitir los datos que provienen del DAO" {
        runTest {
            val postsFromDao = listOf(Post(1, 1, "From DAO", "Body"))
            every { mockDao.getAllPosts() } returns flowOf(postsFromDao)

            val newRepository = PostRepository(mockDao, mockApi)
            val result = newRepository.posts.first()

            result shouldBe postsFromDao
        }
    }

    "refreshPosts debe obtener datos de la API, borrarlos y guardarlos en el DAO" {
        runTest {
            val postsFromApi = listOf(Post(1, 1, "API Post", "Body"))
            coEvery { mockApi.getPosts() } returns postsFromApi
            coEvery { mockDao.deleteAllPosts() } returns Unit
            coEvery { mockDao.insertAll(any()) } returns Unit

            repository.refreshPosts()

            coVerify(exactly = 1) { mockApi.getPosts() }
            coVerify(exactly = 1) { mockDao.deleteAllPosts() }
            coVerify(exactly = 1) { mockDao.insertAll(postsFromApi) }
        }
    }

    "si la API falla, refreshPosts no debe interactuar con el DAO" {
        runTest {
            val apiException = RuntimeException("Error de red")
            coEvery { mockApi.getPosts() } throws apiException

            repository.refreshPosts()

            // Assert: Verificamos que se llamó a Log.e
            coVerify(exactly = 1) { Log.e("PostRepository", "Error refreshing posts", apiException) }
            // Y que NO se interactuó con el DAO
            coVerify(exactly = 0) { mockDao.deleteAllPosts() }
            coVerify(exactly = 0) { mockDao.insertAll(any()) }
        }
    }
})

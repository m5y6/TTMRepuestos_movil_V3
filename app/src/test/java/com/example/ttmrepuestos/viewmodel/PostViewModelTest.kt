package com.example.ttmrepuestos.viewmodel

import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
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
class PostViewModelTest : StringSpec({

    lateinit var mockRepository: PostRepository
    lateinit var viewModel: PostViewModel
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        every { mockRepository.posts } returns flowOf(emptyList())
        coEvery { mockRepository.refreshPosts() } returns Unit
        viewModel = PostViewModel(mockRepository, testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "el estado inicial de posts debe ser una lista vacía" {
        viewModel.posts.value.shouldBeEmpty()
    }

    "el ViewModel debe llamar a refreshPosts en el init" {
        runTest(testDispatcher) {
            advanceUntilIdle()
            coVerify(exactly = 1) { mockRepository.refreshPosts() }
        }
    }

    "el StateFlow de posts debe emitir los datos del repositorio" {
        runTest(testDispatcher) {
            // Arrange
            val fakePosts = listOf(Post(1, 1, "Titulo 1", "Cuerpo 1"))
            every { mockRepository.posts } returns flowOf(fakePosts)

            // Act
            val newViewModel = PostViewModel(mockRepository, testDispatcher)

            // CORRECCIÓN: Damos tiempo a la corrutina de stateIn para ejecutarse
            advanceUntilIdle()

            // Assert
            newViewModel.posts.value shouldBe fakePosts
        }
    }
})

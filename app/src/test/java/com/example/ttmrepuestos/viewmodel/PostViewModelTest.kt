package com.example.ttmrepuestos.viewmodel

import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.remote.ApiService
import com.example.ttmrepuestos.repository.PostRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class TestTablePostRepository(private val testApi: ApiService): PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApi.getPosts()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : StringSpec({
    "postList se actualiza correctamente tras fetchPosts()" {
        val fakePosts = listOf(
            Post(1, 1, "Titulo 1", "Contenido 1"),
            Post(2, 2, "Titulo 2", "Contenido 2")
        )

        // MOCKEAMOS EL REPOSITORIO DIRECTAMENTE (Más limpio)
        val mockRepo = mockk<PostRepository>()
        coEvery { mockRepo.getPosts() } returns fakePosts

        val dispatcher = StandardTestDispatcher()
        // Pasamos el mockRepo en lugar de la clase auxiliar
        val viewModel = PostViewModel(mockRepo, dispatcher)

        runTest(dispatcher) {
            viewModel.fetchPosts()
            advanceUntilIdle()
            viewModel.postList.value shouldContainExactly fakePosts
        }
    }
})

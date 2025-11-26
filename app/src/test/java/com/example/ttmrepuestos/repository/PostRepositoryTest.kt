package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.remote.ApiService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class TestTablePostRepository(private val testApi: ApiService): PostRepository() {
    override suspend fun getPosts(): List<Post> {
        return testApi.getPosts()
    }
}

class PostRepositoryTest : StringSpec({
    "getPosts() debe retornar una lista de posts simulada" {
        val fakePosts = listOf(
            Post(1, 1, "Titulo 1", "Cuerpo 1"),
            Post(2, 2, "Titulo 2", "Cuerpo 2")
        )
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } returns fakePosts
        val repo = TestTablePostRepository(mockApi)
        runTest {
            val result = repo.getPosts()
            result shouldContainExactly fakePosts
        }
    }
})

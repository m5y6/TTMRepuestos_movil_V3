package com.example.ttmrepuestos.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.repository.PostRepository
import com.example.ttmrepuestos.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FakePostRepository(private val posts: List<Post>) : PostRepository() {
    override suspend fun getPosts(): List<Post> = posts
}

@OptIn(ExperimentalCoroutinesApi::class)
class PostScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() = runTest {
        // Datos simulados
        val fakePosts = listOf(
            Post(1, 1, "Titulo 1", "Cuerpo 1"),
            Post(2, 2, "Titulo 2", "Cuerpo 2")
        )
        // Dispatcher de test
        val dispatcher = StandardTestDispatcher(testScheduler)
        // Repositorio falso
        val fakeRepo = FakePostRepository(fakePosts)
        // ViewModel real pero controlado
        val viewModel = PostViewModel(fakeRepo, dispatcher)

        composeRule.setContent {
            PostScreen(viewModel = viewModel)
        }
        
        // Avanza la corrutina del init { fetchPosts() }
        advanceUntilIdle()

        composeRule.onNodeWithText("Titulo: Titulo 1").assertIsDisplayed()
        composeRule.onNodeWithText("Titulo: Titulo 2").assertIsDisplayed()
    }
}

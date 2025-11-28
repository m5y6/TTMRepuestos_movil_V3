package com.example.ttmrepuestos.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.repository.PostRepository
import com.example.ttmrepuestos.ui.screens.PostScreen
import com.example.ttmrepuestos.viewmodel.PostViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun el_titulo_de_post_debe_aparecer_en_pantalla() = runTest {
        // 1. Datos simulados que queremos que nuestro Flow emita
        val fakePosts = listOf(
            Post(1, 1, "Titulo 1", "Cuerpo 1"),
            Post(2, 2, "Titulo 2", "Cuerpo 2")
        )

        // 2. Creamos un mock del PostRepository real
        val mockRepo = mockk<PostRepository>()

        // 3. Le decimos al mock qué debe devolver cuando se interactúe con él:
        //    - Cuando se acceda a la propiedad `posts`, debe devolver un Flow con nuestros datos falsos.
        coEvery { mockRepo.posts } returns flowOf(fakePosts)
        //    - Cuando se llame a la función `refreshPosts`, no debe hacer nada.
        coEvery { mockRepo.refreshPosts() } returns Unit

        // 4. Creamos el ViewModel con el repositorio mockeado
        val dispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = PostViewModel(mockRepo, dispatcher)

        // 5. Lanzamos la UI con el ViewModel
        composeRule.setContent {
            PostScreen(viewModel = viewModel)
        }

        // 6. Avanzamos el tiempo para que se ejecuten las corrutinas del ViewModel (el refresh inicial)
        advanceUntilIdle()

        // 7. Verificamos que los datos del Flow se han pintado en la pantalla
        composeRule.onNodeWithText("Titulo: Titulo 1").assertIsDisplayed()
        composeRule.onNodeWithText("Titulo: Titulo 2").assertIsDisplayed()
    }
}

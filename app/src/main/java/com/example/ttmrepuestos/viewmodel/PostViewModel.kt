package com.example.ttmrepuestos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val posts: StateFlow<List<Post>> = repository.posts
        .catch { exception ->
            // Aquí podrías manejar errores
        }
        .stateIn(
            scope = viewModelScope,
            // CORRECCIÓN: Eagerly inicia la recolección inmediatamente, haciendo el testing más fácil.
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch(dispatcher) {
            repository.refreshPosts()
        }
    }
}

class PostViewModelFactory(
    private val repository: PostRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(repository, dispatcher) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

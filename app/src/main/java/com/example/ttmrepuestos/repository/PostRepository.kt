package com.example.ttmrepuestos.repository

import android.util.Log
import com.example.ttmrepuestos.data.local.PostDao
import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.network.ApiService
import kotlinx.coroutines.flow.Flow

// Hacemos la clase open para que pueda ser extendida en los tests
open class PostRepository(
    private val dao: PostDao,
    private val apiService: ApiService
) {
    // El Flow de posts se obtiene directamente desde la base de datos local (DAO)
    val posts: Flow<List<Post>> = dao.getAllPosts()

    // Hacemos la función open para que pueda ser sobrescrita o verificada en los tests
    open suspend fun refreshPosts() {
        try {
            // 1. Obtiene los posts desde la red
            val remotePosts = apiService.getPosts()
            // 2. Borra los posts locales
            dao.deleteAllPosts()
            // 3. Inserta los nuevos posts en la base de datos
            dao.insertAll(remotePosts)
        } catch (e: Exception) {
            // Maneja posibles errores de red
            Log.e("PostRepository", "Error refreshing posts", e)
        }
    }
}

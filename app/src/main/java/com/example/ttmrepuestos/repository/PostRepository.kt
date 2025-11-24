package com.example.ttmrepuestos.repository

import com.example.ttmrepuestos.model.Post
import com.example.ttmrepuestos.remote.RetrofitInstance

open class PostRepository {
    open suspend fun getPosts(): List<Post>{
        return RetrofitInstance.api.getPosts()
    }
}
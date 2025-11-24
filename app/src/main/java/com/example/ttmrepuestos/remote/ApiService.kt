package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("/posts")
    suspend fun getPosts(): List<Post>
}
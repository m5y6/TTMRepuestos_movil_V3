package com.example.ttmrepuestos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UsuarioRetrofitInstance {
    val api: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/m5y6/1b80905c9f7d8ba890f6795cf8c66a78/raw/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }
}

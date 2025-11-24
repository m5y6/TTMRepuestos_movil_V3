package com.example.ttmrepuestos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/m5y6/603691374de5380a8596f75e1e0d90ea/raw/ce71e0a7b4de02412ac35ed95caa0f315c8f19f9/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
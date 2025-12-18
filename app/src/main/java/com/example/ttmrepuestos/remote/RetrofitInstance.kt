package com.example.ttmrepuestos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Apuntamos a la IP pública y puerto de tu servidor EC2
    private const val BASE_URL = "http://100.30.39.133:3000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private const val CURRENCY_BASE_URL = "https://v6.exchangerate-api.com/"
    val currencyApi: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(CURRENCY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }

}

package com.example.ttmrepuestos.remote

import com.example.ttmrepuestos.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    // Ejemplo de llamada: v6/TU_API_KEY/latest/USD
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String = "CLP" // Peso Chileno como base
    ): Response<CurrencyResponse>
}
    
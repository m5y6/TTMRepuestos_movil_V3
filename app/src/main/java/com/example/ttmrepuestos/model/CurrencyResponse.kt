package com.example.ttmrepuestos.model

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>
)
    
package com.example.ttmrepuestos.network.response

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("message") val message: String
)

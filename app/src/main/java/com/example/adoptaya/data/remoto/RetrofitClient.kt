package com.example.adoptaya.data.remoto

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://adoptaya-backend.onrender.com/"

    val apiService: AdoptayaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdoptayaApiService::class.java)
    }
}
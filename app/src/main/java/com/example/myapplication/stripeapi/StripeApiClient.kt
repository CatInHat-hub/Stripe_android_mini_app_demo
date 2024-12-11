package com.example.myapplication.stripeapi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StripeApiClient {
    private const val BASE_URL="https://seasoned-maroon-voice.glitch.me"

    private val retrofit:Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    val stripeApiService: StripeApiService by lazy {
        retrofit.create(StripeApiService::class.java)
    }
}
package com.example.m16.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://www.boredapi.com"

object RetrofitServices {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val searchUsefulApi: SearchUsefulApi = retrofit.create(
        SearchUsefulApi::class.java
    )
}

interface SearchUsefulApi {
    @GET("/api/activity/")
    suspend fun getResultsOfUseful(): Response<UsefulActivityDto>
}
package com.example.m14

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://randomuser.me/"

object RetrofitServices {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val searchUsersApi: SearchUsersApi = retrofit.create(
        SearchUsersApi::class.java
    )
}

interface SearchUsersApi {
    @GET("api")
    suspend fun getResultsOfUser(): Response<UsersModel>
}
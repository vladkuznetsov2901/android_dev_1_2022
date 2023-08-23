package com.example.m17.model.repository.Api

import com.example.m17.data.PhotosActivity
import com.example.m17.data.PhotosList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


private const val BASE_URL = "https://api.nasa.gov/mars-photos/"

object RetrofitServices {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val searchPhotosApi: SearchPhotosApi = retrofit.create(
        SearchPhotosApi::class.java
    )
}

interface SearchPhotosApi {
    @GET("api/v1/rovers/curiosity/photos?sol=1000&api_key=$api_key")
    suspend fun getResultsOfPhotos(@Query("page") page: Int): Response<PhotosActivity>

    companion object {
        private const val api_key = "Rwv4O7BOxTKpO08KGCM0zR0HPNllaqAqynW4SIL8"
    }
}

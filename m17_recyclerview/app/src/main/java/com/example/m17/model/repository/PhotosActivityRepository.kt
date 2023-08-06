package com.example.m17.model.repository

import com.example.m17.data.Photo
import com.example.m17.data.PhotosActivity
import com.example.m17.model.repository.Api.RetrofitServices
import com.example.m17.model.repository.Api.SearchPhotosApi
import kotlinx.coroutines.delay
import javax.inject.Inject

class PhotosActivityRepository @Inject constructor() {

    suspend fun getSearchPhotosApi(page: Int): List<Photo> {
        delay(3000)
        val response = RetrofitServices.searchPhotosApi.getResultsOfPhotos(page)
        return response.body()!!.photos
    }

}

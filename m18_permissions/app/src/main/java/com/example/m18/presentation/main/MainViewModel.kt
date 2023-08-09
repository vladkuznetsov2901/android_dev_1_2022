package com.example.m18.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m18.data.Photo
import com.example.m18.data.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: PhotoRepository) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()

    var allPhotos = repository.getAllPhotos()

    init {
        getPhotos()
    }
    suspend fun insertPhoto(photo: Photo) {

        withContext(Dispatchers.IO) {
            repository.insert(photo)
        }
    }

    private fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getAllPhotos()
            }.fold(
                onSuccess = {photos -> updatePhotos(photos) },
                onFailure = { throwable ->
                    Log.e(
                        "PhotosListViewModel",
                        "Error fetching photos: ${throwable.message}",
                        throwable
                    )
                }
            )
        }
    }

    private fun updatePhotos(photos: List<Photo>) {
        _photos.value = photos
    }





}
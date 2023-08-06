package com.example.m17.presentation.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.m17.data.Photo
import com.example.m17.model.repository.PhotosActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val photosActivityRepository: PhotosActivityRepository,
) : ViewModel() {

    private val source = PhotosPagingSource()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()

//    val pagedPhotos: Flow<PagingData<Photo>> = Pager(
//        config = PagingConfig(pageSize = 10),
//        pagingSourceFactory = { PhotosPagingSource() }
//    ).flow.cachedIn(viewModelScope)

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                photosActivityRepository.getSearchPhotosApi(source.page)
            }.fold(
                onSuccess = { photos -> updatePhotos(photos) },
                onFailure = { throwable ->
                    Log.e(
                        "PhotosListViewModel",
                        "Error fetching photos: ${throwable.message}",
                        throwable
                    )
                }
            )
            _isLoading.value = false
        }
    }

    private fun updatePhotos(photos: List<Photo>) {
        _photos.value = photos
    }


}
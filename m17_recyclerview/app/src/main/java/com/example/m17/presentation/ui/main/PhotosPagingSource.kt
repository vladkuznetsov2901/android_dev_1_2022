package com.example.m17.presentation.ui.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.m17.data.Photo
import com.example.m17.model.repository.PhotosActivityRepository

class PhotosPagingSource : PagingSource<Int, Photo>() {

    var page = 0

    private val repository = PhotosActivityRepository()
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        page = params.key ?: 1
        return kotlin.runCatching {
            repository.getSearchPhotosApi(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )

    }

    companion object {
        const val FIRST_PAGE = 1
    }

}
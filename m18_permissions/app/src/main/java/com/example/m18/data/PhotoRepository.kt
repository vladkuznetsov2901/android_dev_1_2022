package com.example.m18.data

import javax.inject.Inject

class PhotoRepository @Inject constructor(private val photoDAO: PhotoDAO) {

    fun getAllPhotos(): List<Photo> {
        return photoDAO.getAllPhotos()
    }

    fun insert(photo: Photo) {
        photoDAO.insert(photo)
    }

}
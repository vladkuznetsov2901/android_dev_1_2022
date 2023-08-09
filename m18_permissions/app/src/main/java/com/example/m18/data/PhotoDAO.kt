package com.example.m18.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query



@Dao
interface PhotoDAO {

    @Query("SELECT * FROM photoSRC")
    fun getAllPhotos(): List<Photo>

    @Insert(entity = Photo::class)
    fun insert(photo: Photo)

}
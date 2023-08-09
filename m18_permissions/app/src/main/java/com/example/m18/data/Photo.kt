package com.example.m18.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photoSRC")
data class Photo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "src")
    val src: String,

    ) {
    constructor(src: String): this(0, src)
}
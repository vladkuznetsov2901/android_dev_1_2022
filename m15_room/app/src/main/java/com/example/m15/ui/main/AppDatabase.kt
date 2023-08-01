package com.example.m15.ui.main

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

}
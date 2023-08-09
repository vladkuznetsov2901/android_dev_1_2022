package com.example.m18

import android.app.Application
import androidx.room.Room
import com.example.m18.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "photoSRC"
        ).build()
    }

}
package com.example.m18.di

import android.app.Application
import com.example.m18.data.AppDatabase
import com.example.m18.data.PhotoDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getAppDB(context: Application): AppDatabase {
        return AppDatabase.getAppDB(context)
    }
    @Provides
    @Singleton
    fun getDao(database: AppDatabase): PhotoDAO {
        return database.photoDao()
    }
}
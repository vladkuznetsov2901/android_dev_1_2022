package com.example.m18.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Photo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {


    abstract fun photoDao(): PhotoDAO

    companion object {
        private var dbINSTANCE: AppDatabase? = null
        fun getAppDB(context: Context): AppDatabase {
            if (dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext, AppDatabase::class.java, name = "db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dbINSTANCE!!
        }
    }


}
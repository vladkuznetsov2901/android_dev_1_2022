package com.example.m15.ui.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM word LIMIT 5")
    fun getFirstFive(): Flow<List<Word>>

    @Insert(entity = Word::class)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word")
    suspend fun clearAll()

    @Update
    fun update(word: Word)

    @Query("SELECT COUNT(*) FROM word WHERE id = :id")
    suspend fun checkIfWordExists(id: String): Int

    @Query("SELECT * FROM word WHERE id = :id")
    suspend fun getWordById(id: String): Word?

}

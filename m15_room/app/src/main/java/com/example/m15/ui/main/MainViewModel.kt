package com.example.m15.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val wordDao: WordDao) :
    ViewModel() {

    val toastMessage: LiveData<String> = MutableLiveData()

    val allWords: Flow<List<Word>> = this.wordDao.getFirstFive()
    suspend fun updateOrInsertWord(word: String) {
        withContext(Dispatchers.IO) {
            val wordExists = wordDao.checkIfWordExists(word) > 0
            if (wordExists) {
                val existingWord = wordDao.getWordById(word)
                existingWord?.let {
                    val updatedCount = it.count + 1
                    wordDao.update(
                        Word(
                            id = word,
                            count = updatedCount
                        )
                    )
                }
            } else {
                if (word.all { char -> char.isLetter() } and word.isNotBlank() and word.isNotEmpty()) {
                    val newWord = Word(id = word, count = 1)
                    wordDao.insert(newWord)
                } else {

                    viewModelScope.launch(Dispatchers.Main) {
                        (toastMessage as MutableLiveData).value = "You can add only words!"
                    }
                }

            }
        }
    }

    fun clearAllWords() {
        viewModelScope.launch {
            wordDao.clearAll()
        }
    }


}
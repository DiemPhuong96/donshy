package com.example.donshy.data.repository

import com.example.donshy.data.model.Word
import com.example.donshy.utils.Result

interface HomeRepository {
    suspend fun addWord(word: Word): Result<Boolean>
    suspend fun getWord(): Result<List<Word>>
}
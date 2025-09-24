package com.example.donshy.data.repository

import com.example.donshy.data.model.Word
import com.example.donshy.utils.Result
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class HomeRepository {
    private val db = Firebase.firestore
    private val wordsCollection = db.collection("words")
    suspend fun addWord(word: Word): Result<Boolean> = try {
        wordsCollection.add(word).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e.message ?: "unknow error")
    }

    suspend fun getWord(): Result<List<Word>> = try {
        val snapshot = wordsCollection.get().await()
        val words = snapshot.toObjects(Word::class.java)
        Result.Success(words)
    } catch (e: Exception) {
        Result.Error(e.message ?: "unknow error")
    }
}

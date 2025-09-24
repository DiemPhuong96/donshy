package com.example.donshy.data.repository

import com.example.donshy.utils.Result
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TranslateRepository {
    suspend fun translateText(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val translator = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build()
                .let { Translation.getClient(it) }

            translator.downloadModelIfNeeded().await()
            val translatedText = translator.translate(text).await()
            translator.close()
            Result.Success(translatedText)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }
}
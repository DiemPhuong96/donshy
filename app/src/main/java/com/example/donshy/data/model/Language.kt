package com.example.donshy.data.model

import com.google.mlkit.nl.translate.TranslateLanguage

data class Language(val code: String, val displayName: String)

object LanguageProvider {
    fun getSupportedLanguages(): List<Language> {
        return listOf(
            Language(TranslateLanguage.ENGLISH, "English"),
            Language(TranslateLanguage.VIETNAMESE, "VietNames"),
            Language(TranslateLanguage.JAPANESE, "Japanese"),
            Language(TranslateLanguage.CHINESE, "Chinese"),
            Language(TranslateLanguage.KOREAN, "Korean"),
            Language(TranslateLanguage.FRENCH, "French")
        )
    }

}
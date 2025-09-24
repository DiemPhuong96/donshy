package com.example.donshy.ViewModel.translate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donshy.data.repository.TranslateRepository
import com.example.donshy.utils.Result
import kotlinx.coroutines.launch

class TranslateViewModel(private val translateRepository: TranslateRepository) : ViewModel() {
    private val _translatedText = MutableLiveData< Result<String>>()
    val translateResult get() = _translatedText
    fun translateText(text: String, sourceLanguage: String, targetLanguage: String) {
        translateResult.value = Result.Loading
        viewModelScope.launch {
            _translatedText.value =
                translateRepository.translateText(text, sourceLanguage, targetLanguage)
        }
    }
}
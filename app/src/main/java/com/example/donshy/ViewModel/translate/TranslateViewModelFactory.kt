package com.example.donshy.ViewModel.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.TranslateRepository

class TranslateViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = TranslateRepository()
        return TranslateViewModel(repository) as T
    }
}
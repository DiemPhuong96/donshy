package com.example.donshy.ViewModel.audio

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.AudioRepository

class AudioViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AudioRepository(context)
        return AudioViewModel(repository) as T
    }

}

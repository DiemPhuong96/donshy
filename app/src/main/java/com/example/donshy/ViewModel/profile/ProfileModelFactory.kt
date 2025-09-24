package com.example.donshy.ViewModel.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.ProfileRepository

class ProfileModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = ProfileRepository(context)
        return ProfileViewModel(repository) as T
    }
}
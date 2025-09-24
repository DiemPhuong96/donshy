package com.example.donshy.ViewModel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.HomeRepository

class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = HomeRepository()
        return HomeViewModel(repository) as T
    }
}

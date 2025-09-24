package com.example.donshy.ViewModel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.SignUpRepository
import com.google.firebase.auth.FirebaseAuth

class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = SignUpRepository(FirebaseAuth.getInstance())
        return SignUpViewModel(repository) as T
    }
}
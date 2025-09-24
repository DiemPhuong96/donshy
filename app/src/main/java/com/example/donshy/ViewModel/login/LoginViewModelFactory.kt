package com.example.donshy.ViewModel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.data.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = LoginRepository(FirebaseAuth.getInstance())
        return LoginViewModel(repository) as T
    }
}
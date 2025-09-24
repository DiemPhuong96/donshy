package com.example.donshy.ViewModel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donshy.data.model.AuthResponse
import com.example.donshy.data.model.User
import com.example.donshy.data.repository.LoginRepository
import com.example.donshy.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository): ViewModel() {
    private var _loginState =  MutableLiveData<Result<AuthResponse>>()
    val loginState: LiveData<Result<AuthResponse>> get() = _loginState
    fun login(email: String, password: String) {
        _loginState.value = Result.Loading
        val user = User(email = email, password = password)
        viewModelScope.launch {
            val result = repository.login(user)
            _loginState.value = result
        }
    }
}
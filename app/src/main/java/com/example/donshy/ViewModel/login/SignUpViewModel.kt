package com.example.donshy.ViewModel.login

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donshy.data.model.AuthResponse
import com.example.donshy.data.model.User
import com.example.donshy.data.repository.SignUpRepository
import com.example.donshy.utils.Result
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {
    private val _registerState = MutableLiveData<Result<AuthResponse>>()
    val registerState: LiveData<Result<AuthResponse>> = _registerState
    private val _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> get() = _emailVerified
    private var pollingRunnable: Runnable? = null
    var handler: Handler? = null

    fun register(name: String, email: String, password: String) {
        _registerState.value = Result.Loading
        val user = User(name, email, password)
        viewModelScope.launch {
            val result = repository.registerUser(user)
            _registerState.value = result
        }
    }

    fun startEmailCheckPolling(intervalMillis: Long = 5000L) {
        handler = Handler(Looper.getMainLooper())
        pollingRunnable = Runnable {
            viewModelScope.launch {
                val verified = repository.isEmailVerified()
                if (verified) {
                    _emailVerified.value = true
                    stopEmailVerificationPolling()
                } else {
                    handler?.postDelayed(pollingRunnable!!, intervalMillis)
                }
            }
        }
        handler?.post(pollingRunnable!!)
    }
    fun stopEmailVerificationPolling() {
        handler?.removeCallbacks(pollingRunnable!!)
        handler = null
        pollingRunnable = null
    }

    override fun onCleared() {
        stopEmailVerificationPolling()
    }
}
package com.example.donshy.ViewModel.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donshy.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted get() = _isDeleted
    private val _savedProfileImage = MutableLiveData<File?>()
    val savedProfileImage get() = _savedProfileImage
    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout get() = _isLogout

    fun saveImageFileLocally(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = repository.saveImageToLocal(uri)
            _savedProfileImage.postValue(file)
        }
    }

    fun loadAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            val file = repository.loadImageFromLocal()
            _savedProfileImage.postValue(file)
        }
    }

    fun deleteProfileImage() {
        viewModelScope.launch(Dispatchers.IO) {
            val isDeleted = repository.deleteLocalImage()
            _isDeleted.postValue(isDeleted)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _isLogout.postValue(true)
    }
}
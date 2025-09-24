package com.example.donshy.ViewModel.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donshy.data.model.Audio
import com.example.donshy.data.repository.AudioRepository

class AudioViewModel(private val repository: AudioRepository) : ViewModel() {
    private val _audioPath = MutableLiveData<List<Audio>>()
    val audioPath: LiveData<List<Audio>> get() = _audioPath
    fun loadAudio(type: String, fileName: String) {
       // _audioPath.value = repository.getAudioAssetPath(type, fileName)
    }

    fun loadAllAudio(type: String) {
        _audioPath.value = repository.getAllAudioAssetPath(type)
    }
}
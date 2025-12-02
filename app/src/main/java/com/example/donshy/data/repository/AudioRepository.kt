package com.example.donshy.data.repository

import com.example.donshy.data.model.Audio

interface AudioRepository {
    fun getAllAudioAssetPath (type: String): List<Audio>
}
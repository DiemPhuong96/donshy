package com.example.donshy.data.repository

import android.content.Context
import com.example.donshy.data.model.Audio

class AudioRepository(private val context: Context) {
    fun getAudioAssetPath(type: String, fileName: String): String {
        return "asset:///$type/$fileName"
    }

    fun getAllAudioAssetPath(type: String): List<Audio> {
        return try {
            context.assets.list(type)?.map { fileName ->
                Audio(
                    name = fileName.substringBeforeLast("."),
                    path = "file:///android_asset/$type/$fileName",
                    type = type
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

package com.example.donshy.data.repository

import android.content.Context
import com.example.donshy.data.model.Audio
import javax.inject.Inject

class AudioRepositoryImpl (private val context: Context): AudioRepository {
    fun getAudioAssetPath(type: String, fileName: String): String {
        return "asset:///$type/$fileName"
    }

    override fun getAllAudioAssetPath(type: String): List<Audio> {
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

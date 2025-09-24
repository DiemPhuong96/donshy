package com.example.donshy.data.repository

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import android.net.Uri

class ProfileRepository(private val context: Context) {
    private val profileImageName = "profile.jpg"

    fun saveImageToLocal(uri: Uri): File? {
        return try {
            val file = File(context.filesDir, profileImageName)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file, false).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadImageFromLocal(): File? {
        val file = File(context.filesDir, profileImageName)
        return if (file.exists()) file else null
    }

    fun deleteLocalImage(): Boolean {
        return loadImageFromLocal()?.delete() == true
    }
}
package com.example.contacts.data.manager

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun uriToFile(uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File.createTempFile("temp_image_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
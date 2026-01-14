package com.example.contacts.data.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
            val tempFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)

            // 1. Orijinal Boyut Logu
            val originalSize = contentResolver.openAssetFileDescriptor(uri, "r")?.length ?: 0
            Log.d("CompressionTest", "Orijinal Boyut: ${originalSize / 1024} KB")

            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val outputStream = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
            outputStream.flush()
            outputStream.close()

            // 2. Sıkıştırılmış Boyut Logu
            val compressedSize = tempFile.length()
            Log.d("CompressionTest", "Sıkıştırılmış Boyut: ${compressedSize / 1024} KB")

            tempFile
        } catch (e: Exception) {
            Log.e("CompressionTest", "Hata: ${e.message}")
            null
        }
    }
}
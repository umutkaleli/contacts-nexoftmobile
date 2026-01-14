package com.example.contacts.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createTempPictureUri(context: Context): Uri {
    val tempFile = File.createTempFile(
        "picture_${System.currentTimeMillis()}",
        ".jpg",
        context.cacheDir
    ).apply {
        createNewFile()
        deleteOnExit()
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tempFile
    )
}
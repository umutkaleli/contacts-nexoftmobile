package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(
    @SerializedName("imageUrl")
    val imageUrl: String?
)
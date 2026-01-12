package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName


data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("messages")
    val messages: List<String>?,

    @SerializedName("status")
    val status: HttpStatusCode?,

    @SerializedName("data")
    val data: T?
)
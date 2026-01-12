package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?
)
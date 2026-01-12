package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: String?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,

    @SerializedName("createdAt")
    val createdAt: String?
)
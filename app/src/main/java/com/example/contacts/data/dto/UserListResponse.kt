package com.example.contacts.data.dto

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("users")
    val users: List<UserResponse>?
)
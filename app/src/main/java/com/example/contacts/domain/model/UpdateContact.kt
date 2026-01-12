package com.example.contacts.domain.model

data class UpdateContact(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String?
)

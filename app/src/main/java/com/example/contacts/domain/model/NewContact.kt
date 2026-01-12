package com.example.contacts.domain.model

data class NewContact(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String?
)
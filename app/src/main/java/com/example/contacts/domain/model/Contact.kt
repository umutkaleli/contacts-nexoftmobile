package com.example.contacts.domain.model

data class Contact(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val profileImageUrl: String?,
    // Determines if the contact exists in the local device storage.
    val isSavedInDevice: Boolean? = false
) {
    // Extra property to show full name if UI needs it.
    val fullName: String
        get() = "$firstName $lastName"
}

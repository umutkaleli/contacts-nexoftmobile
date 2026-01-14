package com.example.contacts.ui.screens.addcontact

import android.net.Uri

data class AddContactState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSavedSuccessfully: Boolean = false
)
package com.example.contacts.ui.screens.editcontact

import android.net.Uri

data class EditContactState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",


    val currentImageUrl: String? = null,

    val newSelectedImageUri: Uri? = null,

    val error: String? = null,
    val isUpdatedSuccessfully: Boolean = false
)
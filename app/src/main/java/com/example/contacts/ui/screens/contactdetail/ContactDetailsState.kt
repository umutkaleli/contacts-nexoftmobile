package com.example.contacts.ui.screens.contactdetail

import com.example.contacts.domain.model.Contact

data class ContactDetailState(
    val isLoading: Boolean = false,

    val contact: Contact? = null,

    val error: String? = null,

    val isDeleted: Boolean = false,

    val isSavedToDevice: Boolean = false,

    val showDeleteSheet: Boolean = false
)
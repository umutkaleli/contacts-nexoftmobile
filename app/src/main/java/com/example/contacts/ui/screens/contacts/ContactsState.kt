package com.example.contacts.ui.screens.contacts

import com.example.contacts.domain.model.Contact

data class ContactsState(
    val isLoading: Boolean = false,
    val contacts: List<Contact> = emptyList(),
    val error: String? = null
)
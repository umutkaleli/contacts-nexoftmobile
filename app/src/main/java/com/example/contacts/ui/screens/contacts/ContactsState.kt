package com.example.contacts.ui.screens.contacts

import com.example.contacts.domain.model.Contact

data class ContactsState(
    val isLoading: Boolean = false,
    val contacts: List<Contact> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",

    val contactToDelete: Contact? = null,
    val showDeleteSheet: Boolean = false,

    val isSearchActive: Boolean = false,

    val searchHistory: List<String> = emptyList(),

    val isNoResults: Boolean = false
)

// Effect (toast message)
sealed interface ContactsUiEffect {
    data class ShowToast(val message: String) : ContactsUiEffect
}
package com.example.contacts.ui.screens.contacts

import com.example.contacts.domain.model.Contact

sealed interface ContactsEvent {
    data class OnSearchQueryChanged(val query: String) : ContactsEvent

    data class OnDeleteContactClick(val contact: Contact) : ContactsEvent
    data object OnDismissDeleteSheet : ContactsEvent
    data object OnConfirmDelete : ContactsEvent

    data object LoadContacts : ContactsEvent

    data object CheckDeviceContacts : ContactsEvent

    data class OnSearchFocusChange(val isFocused: Boolean) : ContactsEvent

    data class OnSearchHistoryItemClick(val query: String) : ContactsEvent

    data class OnDeleteSearchHistoryItem(val query: String) : ContactsEvent

    data object OnClearSearchHistory : ContactsEvent
}
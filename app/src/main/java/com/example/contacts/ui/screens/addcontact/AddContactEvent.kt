package com.example.contacts.ui.screens.addcontact

import android.net.Uri

sealed interface AddContactEvent {
    data class EnteredFirstName(val value: String) : AddContactEvent
    data class EnteredLastName(val value: String) : AddContactEvent
    data class EnteredPhoneNumber(val value: String) : AddContactEvent
    data class SelectedImage(val uri: Uri?) : AddContactEvent
    data object SaveContact : AddContactEvent
}
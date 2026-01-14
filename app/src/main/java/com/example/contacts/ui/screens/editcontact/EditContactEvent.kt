package com.example.contacts.ui.screens.editcontact

import android.net.Uri

sealed interface EditContactEvent {
    data class EnteredFirstName(val value: String) : EditContactEvent
    data class EnteredLastName(val value: String) : EditContactEvent
    data class EnteredPhoneNumber(val value: String) : EditContactEvent
    data class SelectedImage(val uri: Uri?) : EditContactEvent
    data object UpdateContact : EditContactEvent
}
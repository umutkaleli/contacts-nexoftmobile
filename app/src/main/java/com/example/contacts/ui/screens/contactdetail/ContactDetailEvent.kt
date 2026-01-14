package com.example.contacts.ui.screens.contactdetail

sealed interface ContactDetailEvent {
    data object DeleteMenuClicked : ContactDetailEvent

    data object ConfirmDelete : ContactDetailEvent

    data object DismissDeleteSheet : ContactDetailEvent

    data object SaveToDevice : ContactDetailEvent

    data object EditContact : ContactDetailEvent

    data object DismissError : ContactDetailEvent
}
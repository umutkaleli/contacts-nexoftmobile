package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.model.NewContact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult

class AddContactUseCase(private val repository: ContactRepository) {

    suspend operator fun invoke(newContact: NewContact): NetworkResult<Unit> {

        if (newContact.firstName.isBlank() || newContact.lastName.isBlank()) {
            return NetworkResult.Error("Name fields cannot be empty.")
        }
        if (newContact.phoneNumber.isBlank()) {
            return NetworkResult.Error("Phone number is required.")
        }

        return repository.addContact(newContact)
    }
}
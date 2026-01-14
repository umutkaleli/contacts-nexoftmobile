package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.model.UpdateContact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(private val repository: ContactRepository) {

    suspend operator fun invoke(updateContact: UpdateContact): NetworkResult<Unit> {

        if (updateContact.firstName.isBlank() && updateContact.lastName.isBlank()) {
            return NetworkResult.Error("Name fields cannot be empty.")
        }

        if (updateContact.phoneNumber.isBlank()) {
            return NetworkResult.Error("Phone number is required.")
        }

        return repository.updateContact(updateContact)
    }
}
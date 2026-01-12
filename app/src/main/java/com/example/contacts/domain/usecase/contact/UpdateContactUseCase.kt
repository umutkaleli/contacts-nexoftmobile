package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult

class UpdateContactUseCase(private val repository: ContactRepository) {

    suspend operator fun invoke(
        id: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        imageUrl: String?
    ): NetworkResult<Unit> {

        if (firstName.isBlank() || lastName.isBlank()) {
            return NetworkResult.Error("Name fields cannot be empty.")
        }
        if (phoneNumber.isBlank()) {
            return NetworkResult.Error("Phone number is required.")
        }

        return repository.updateContact(id, firstName, lastName, phoneNumber, imageUrl)
    }
}
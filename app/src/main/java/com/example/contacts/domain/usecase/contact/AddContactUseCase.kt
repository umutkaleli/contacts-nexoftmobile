package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult

class AddContactUseCase(private val repository: ContactRepository) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        imageUrl: String?
    ): NetworkResult<Unit> {

        // Basic Validation
        if (firstName.isBlank()) {
            return NetworkResult.Error("First name is required.")
        }
        if (lastName.isBlank()) {
            return NetworkResult.Error("Last name is required.")
        }
        if (phoneNumber.isBlank()) {
            return NetworkResult.Error("Phone number is required.")
        }

        return repository.addContact(firstName, lastName, phoneNumber, imageUrl)
    }
}
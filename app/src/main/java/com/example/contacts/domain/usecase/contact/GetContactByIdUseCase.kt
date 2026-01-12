package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult

class GetContactByIdUseCase(private val repository: ContactRepository) {
    suspend operator fun invoke(id: String): NetworkResult<Contact> {
        return repository.getContactById(id)
    }
}
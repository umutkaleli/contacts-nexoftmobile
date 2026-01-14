package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import javax.inject.Inject

class GetContactByIdUseCase @Inject constructor(private val repository: ContactRepository) {
    suspend operator fun invoke(id: String): NetworkResult<Contact> {
        return repository.getContactById(id)
    }
}
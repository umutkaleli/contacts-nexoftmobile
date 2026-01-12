package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult

class DeleteContactUseCase(private val repository: ContactRepository) {
    suspend operator fun invoke(id: String): NetworkResult<Unit> {
        return repository.deleteContact(id)
    }
}
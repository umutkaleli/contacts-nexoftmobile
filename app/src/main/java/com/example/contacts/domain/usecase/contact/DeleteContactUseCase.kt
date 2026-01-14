package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(private val repository: ContactRepository) {
    suspend operator fun invoke(id: String): NetworkResult<Unit> {
        return repository.deleteContact(id)
    }
}
package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetContactsUseCase(
    private val repository: ContactRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<Contact>>> {
        return repository.getContacts().map { result ->
            when (result) {
                is NetworkResult.Success -> {
                    // Sorting alphabetically (A-Z) by first name.
                    val sortedList = result.data?.sortedBy { it.firstName.lowercase() } ?: emptyList()
                    NetworkResult.Success(sortedList)
                }
                else -> result
            }
        }
    }
}
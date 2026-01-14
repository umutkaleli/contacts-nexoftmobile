package com.example.contacts.domain.usecase.search

import com.example.contacts.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class AddSearchUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(query: String) {
        if (query.isNotBlank()) {
            repository.addSearchQuery(query)
        }
    }
}
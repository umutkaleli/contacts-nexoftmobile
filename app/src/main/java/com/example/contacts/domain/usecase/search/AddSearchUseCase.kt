package com.example.contacts.domain.usecase.search

import com.example.contacts.domain.repository.SearchHistoryRepository

class AddSearchUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke(query: String) {
        if (query.isNotBlank()) {
            repository.addSearchQuery(query)
        }
    }
}
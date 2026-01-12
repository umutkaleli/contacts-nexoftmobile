package com.example.contacts.domain.usecase.search

import com.example.contacts.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow


class GetSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getSearchHistory()
    }
}
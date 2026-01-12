package com.example.contacts.domain.usecase.search

import com.example.contacts.domain.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(private val repository: SearchHistoryRepository) {
    suspend operator fun invoke() {
        repository.clearSearchHistory()
    }
}
package com.example.contacts.domain.usecase.search

import com.example.contacts.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class ClearSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke() {
        repository.clearSearchHistory()
    }
}
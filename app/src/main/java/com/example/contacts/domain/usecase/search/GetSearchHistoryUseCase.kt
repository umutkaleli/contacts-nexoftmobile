package com.example.contacts.domain.usecase.search

import com.example.contacts.data.local.entity.SearchHistoryEntity
import com.example.contacts.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<List<SearchHistoryEntity>> {
        return repository.getSearchHistory()
    }
}
package com.example.contacts.domain.repository

import com.example.contacts.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    // Get search history from local database.
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

    // Saves a new search to databse.
    suspend fun addSearchQuery(query: String)

    // Clears a search
    suspend fun deleteSearchQuery(query: String)

    // Clears all search history.
    suspend fun clearSearchHistory()
}
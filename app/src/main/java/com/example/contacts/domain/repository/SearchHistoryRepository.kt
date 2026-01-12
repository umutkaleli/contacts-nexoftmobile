package com.example.contacts.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    // Get search history from local database.
    fun getSearchHistory(): Flow<List<String>>

    // Saves a new search to databse.
    suspend fun addSearchQuery(query: String)

    // Clears all search history.
    suspend fun clearSearchHistory()
}
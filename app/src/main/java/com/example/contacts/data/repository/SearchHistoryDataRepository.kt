package com.example.contacts.data.repository

import com.example.contacts.data.local.dao.SearchHistoryDao
import com.example.contacts.data.local.entity.SearchHistoryEntity
import com.example.contacts.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryDataRepository @Inject constructor(
    private val dao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun getSearchHistory(): Flow<List<SearchHistoryEntity>> {
        return dao.getSearchHistory()
    }

    override suspend fun addSearchQuery(query: String) {
        // Timestamp o anki zaman olacak
        val entity = SearchHistoryEntity(
            query = query,
            timestamp = System.currentTimeMillis()
        )
        dao.insertSearchQuery(entity)
    }

    override suspend fun deleteSearchQuery(query: String) {
        dao.deleteSearchQuery(query)
    }

    override suspend fun clearSearchHistory() {
        dao.clearAllHistory()
    }
}
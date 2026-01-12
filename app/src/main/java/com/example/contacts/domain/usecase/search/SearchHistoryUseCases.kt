package com.example.contacts.domain.usecase.search

// This is a wrapper class to inject all search history related use cases into ViewModel at once.
data class SearchHistoryUseCases(
    val getSearchHistory: GetSearchHistoryUseCase,
    val addSearchQuery: AddSearchUseCase,
    val clearSearchHistory: ClearSearchHistoryUseCase
)
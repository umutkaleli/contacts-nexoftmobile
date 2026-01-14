package com.example.contacts.domain.usecase.search

import javax.inject.Inject

// This is a wrapper class to inject all search history related use cases into ViewModel at once.
data class SearchHistoryUseCases @Inject constructor(
    val getSearchHistory: GetSearchHistoryUseCase,
    val addSearchQuery: AddSearchUseCase,
    val deleteSearchQuery: DeleteSearchUseCase,
    val clearSearchHistory: ClearSearchHistoryUseCase
)
package com.example.contacts.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.data.manager.DeviceContactsManager // Manager Eklendi
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.usecase.contact.ContactUseCases
import com.example.contacts.domain.usecase.search.SearchHistoryUseCases
import com.example.contacts.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases,
    private val searchUseCases: SearchHistoryUseCases,
    private val deviceContactsManager: DeviceContactsManager
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsState())
    val state: StateFlow<ContactsState> = _state.asStateFlow()

    private val _effect = Channel<ContactsUiEffect>()
    val effect = _effect.receiveAsFlow()

    // All contacts without filtered operation
    private var allContacts: List<Contact> = emptyList()

    init {
        getContacts()
        observeSearchHistory()
    }

    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                filterContacts(event.query)
            }
            is ContactsEvent.OnSearchFocusChange -> {
                _state.update { it.copy(isSearchActive = event.isFocused) }
            }
            is ContactsEvent.OnSearchHistoryItemClick -> {
                onEvent(ContactsEvent.OnSearchQueryChanged(event.query))
                onEvent(ContactsEvent.OnSearchFocusChange(true))
            }
            is ContactsEvent.OnClearSearchHistory -> {
                viewModelScope.launch { searchUseCases.clearSearchHistory() }
            }
            is ContactsEvent.OnDeleteSearchHistoryItem -> {
                viewModelScope.launch { searchUseCases.deleteSearchQuery(event.query) }
            }
            is ContactsEvent.OnDeleteContactClick -> {
                _state.update { it.copy(contactToDelete = event.contact, showDeleteSheet = true) }
            }
            is ContactsEvent.OnDismissDeleteSheet -> {
                _state.update { it.copy(contactToDelete = null, showDeleteSheet = false) }
            }
            is ContactsEvent.OnConfirmDelete -> {
                deleteContact()
            }
            is ContactsEvent.CheckDeviceContacts -> {
                refreshDeviceStatus()
            }
        }
    }

    private fun observeSearchHistory() {
        viewModelScope.launch {
            searchUseCases.getSearchHistory().collect { historyList ->
                val historyStrings = historyList.map { it.query }
                _state.update { it.copy(searchHistory = historyStrings) }
            }
        }
    }

    fun saveSearchToHistory(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchUseCases.addSearchQuery(query)
            }
        }
    }

    private fun filterContacts(query: String) {
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    contacts = allContacts,
                    isNoResults = false
                )
            }
            return
        }

        val filteredList = allContacts.filter { contact ->
            val firstNameMatch = contact.firstName?.startsWith(query, ignoreCase = true) == true
            val lastNameMatch = contact.lastName?.startsWith(query, ignoreCase = true) == true

            firstNameMatch || lastNameMatch
        }

        _state.update {
            it.copy(
                contacts = filteredList,
                isNoResults = filteredList.isEmpty()
            )
        }
    }

    private fun refreshDeviceStatus() {
        if (allContacts.isEmpty()) return // Liste boşsa işlem yapma

        viewModelScope.launch {
            allContacts = mapContactsWithDevice(allContacts)

            filterContacts(_state.value.searchQuery)
        }
    }

    private fun getContacts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            contactUseCases.getContacts().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data ?: emptyList()

                        allContacts = mapContactsWithDevice(data).sortedBy { it.firstName }

                        filterContacts(_state.value.searchQuery)
                        _state.update { it.copy(isLoading = false) }
                    }
                    is NetworkResult.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        sendEffect(ContactsUiEffect.ShowToast(result.message ?: "Error"))
                    }
                    is NetworkResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun mapContactsWithDevice(contacts: List<Contact>): List<Contact> {
        val deviceNumbers = deviceContactsManager.getAllDevicePhoneNumbers()

        return contacts.map { contact ->
            val cleanContactNumber = contact.phoneNumber?.replace(Regex("[^0-9]"), "") ?: ""

            val isMatch = cleanContactNumber.isNotBlank() && deviceNumbers.any {
                it.contains(cleanContactNumber) || cleanContactNumber.contains(it)
            }

            contact.copy(isInDevice = isMatch)
        }
    }

    private fun deleteContact() {
        val contact = _state.value.contactToDelete ?: return

        viewModelScope.launch {

            _state.update { it.copy(showDeleteSheet = false) }

            when (val result = contactUseCases.deleteContact(contact.id)) {
                is NetworkResult.Success -> {
                    sendEffect(ContactsUiEffect.ShowToast("✅ User is deleted!"))

                    val updatedList = allContacts.filterNot { it.id == contact.id }
                    allContacts = updatedList

                    if (_state.value.searchQuery.isBlank()) {
                        _state.update {
                            it.copy(
                                contacts = updatedList,
                                contactToDelete = null,
                                isNoResults = false
                            )
                        }
                    } else {
                        filterContacts(_state.value.searchQuery)
                        _state.update { it.copy(contactToDelete = null) }
                    }
                }
                is NetworkResult.Error -> {

                    _state.update { it.copy(isLoading = false) }
                    sendEffect(ContactsUiEffect.ShowToast("Failed: ${result.message}"))
                }
                else -> {}
            }
        }
    }

    private fun sendEffect(effect: ContactsUiEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
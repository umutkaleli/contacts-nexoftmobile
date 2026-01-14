package com.example.contacts.ui.screens.contactdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.data.manager.DeviceContactsManager
import com.example.contacts.domain.usecase.contact.ContactUseCases
import com.example.contacts.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactDetailViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases,
    private val deviceContactsManager: DeviceContactsManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ContactDetailState())
    val state: StateFlow<ContactDetailState> = _state.asStateFlow()

    private val contactId: String? = savedStateHandle["contactId"]
    private var monitorJob: Job? = null

    init {
        loadContact()
    }

    fun onEvent(event: ContactDetailEvent) {
        when (event) {
            is ContactDetailEvent.DeleteMenuClicked -> _state.update { it.copy(showDeleteSheet = true) }
            is ContactDetailEvent.ConfirmDelete -> deleteContact()
            is ContactDetailEvent.DismissDeleteSheet -> _state.update { it.copy(showDeleteSheet = false) }
            is ContactDetailEvent.SaveToDevice -> saveContactToDevice()
            is ContactDetailEvent.DismissError -> _state.update { it.copy(error = null) }
            is ContactDetailEvent.EditContact -> {  }
        }
    }

    private fun loadContact() {
        if (contactId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = contactUseCases.getContactById(contactId)) {
                is NetworkResult.Success -> {
                    val contact = result.data
                    _state.update { it.copy(isLoading = false, contact = contact) }

                    contact?.phoneNumber?.let { monitorContactStatus(it) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun monitorContactStatus(phoneNumber: String) {
        monitorJob?.cancel()
        monitorJob = deviceContactsManager.contactExistsFlow(phoneNumber)
            .onEach { exists ->
                _state.update { it.copy(isSavedToDevice = exists) }
            }
            .launchIn(viewModelScope)
    }

    private fun saveContactToDevice() {
        val contact = state.value.contact ?: return

        viewModelScope.launch {
            val result = deviceContactsManager.saveContactToDevice(
                firstName = contact.firstName,
                lastName = contact.lastName,
                phoneNumber = contact.phoneNumber
            )

            result.onFailure { error ->
                _state.update { it.copy(error = "Failed to save: ${error.localizedMessage}") }
            }
        }
    }

    private fun deleteContact() {
        if (contactId == null) return
        viewModelScope.launch {
            _state.update { it.copy(showDeleteSheet = false, isLoading = true) }
            when (val result = contactUseCases.deleteContact(contactId)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, isDeleted = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }
}
package com.example.contacts.ui.screens.editcontact

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.data.manager.ImageFileManager
import com.example.contacts.domain.model.UpdateContact
import com.example.contacts.domain.usecase.contact.ContactUseCases
import com.example.contacts.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditContactViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases,
    private val imageFileManager: ImageFileManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EditContactState())
    val state: StateFlow<EditContactState> = _state.asStateFlow()

    private val contactId: String? = savedStateHandle["contactId"]

    init {
        loadContactInfo()
    }

    fun onEvent(event: EditContactEvent) {
        when (event) {
            is EditContactEvent.EnteredFirstName -> _state.update { it.copy(firstName = event.value) }
            is EditContactEvent.EnteredLastName -> _state.update { it.copy(lastName = event.value) }
            is EditContactEvent.EnteredPhoneNumber -> _state.update { it.copy(phoneNumber = event.value) }
            is EditContactEvent.SelectedImage -> _state.update { it.copy(newSelectedImageUri = event.uri) }
            is EditContactEvent.UpdateContact -> updateContact()
        }
    }

    private fun loadContactInfo() {
        if (contactId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = contactUseCases.getContactById(contactId)) {
                is NetworkResult.Success -> {
                    result.data?.let { contact ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                firstName = contact.firstName ?: "",
                                lastName = contact.lastName ?: "",
                                phoneNumber = contact.phoneNumber ?: "",
                                currentImageUrl = contact.profileImageUrl
                            )
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private fun updateContact() {
        if (contactId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            var finalImageUrl = _state.value.currentImageUrl

            if (_state.value.newSelectedImageUri != null) {
                val file = imageFileManager.uriToFile(_state.value.newSelectedImageUri!!)
                if (file != null) {
                    when (val uploadResult = contactUseCases.uploadImage(file)) {
                        is NetworkResult.Success -> {
                            finalImageUrl = uploadResult.data
                        }
                        is NetworkResult.Error -> {
                            _state.update { it.copy(isLoading = false, error = "Image Upload Failed") }
                            return@launch
                        }
                        else -> {}
                    }
                }
            }

            val updatedContact = UpdateContact(
                id = contactId,
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                phoneNumber = _state.value.phoneNumber,
                profileImageUrl = finalImageUrl
            )

            when (val result = contactUseCases.updateContact(updatedContact)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, isUpdatedSuccessfully = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }
}
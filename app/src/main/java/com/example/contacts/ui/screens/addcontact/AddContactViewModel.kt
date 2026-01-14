package com.example.contacts.ui.screens.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.data.manager.ImageFileManager // Manager eklendi
import com.example.contacts.domain.model.NewContact
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
class AddContactViewModel @Inject constructor(
    private val contactUseCases: ContactUseCases,
    private val imageFileManager: ImageFileManager
) : ViewModel() {

    private val _state = MutableStateFlow(AddContactState())
    val state: StateFlow<AddContactState> = _state.asStateFlow()

    fun onEvent(event: AddContactEvent) {
        when (event) {
            is AddContactEvent.EnteredFirstName -> _state.update { it.copy(firstName = event.value) }
            is AddContactEvent.EnteredLastName -> _state.update { it.copy(lastName = event.value) }
            is AddContactEvent.EnteredPhoneNumber -> _state.update { it.copy(phoneNumber = event.value) }
            is AddContactEvent.SelectedImage -> _state.update { it.copy(selectedImageUri = event.uri) }
            is AddContactEvent.SaveContact -> saveContact()
        }
    }

    private fun saveContact() {
        val currentState = _state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            var finalImageUrl = ""

            if (currentState.selectedImageUri != null) {

                val file = imageFileManager.uriToFile(currentState.selectedImageUri!!)

                if (file != null) {
                    when (val uploadResult = contactUseCases.uploadImage(file)) {
                        is NetworkResult.Success -> {
                            finalImageUrl = uploadResult.data ?: ""
                        }
                        is NetworkResult.Error -> {
                            _state.update { it.copy(isLoading = false, error = "Image Upload Failed: ${uploadResult.message}") }
                            return@launch
                        }
                        is NetworkResult.Loading -> {}
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Failed to process image file.") }
                    return@launch
                }
            }

            val newContact = NewContact(
                firstName = currentState.firstName,
                lastName = currentState.lastName,
                phoneNumber = currentState.phoneNumber,
                profileImageUrl = finalImageUrl
            )

            when (val result = contactUseCases.addContact(newContact)) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSavedSuccessfully = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                else -> {}
            }
        }
    }
}
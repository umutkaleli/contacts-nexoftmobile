package com.example.contacts.ui.screens.contacts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsState())
    val state: StateFlow<ContactsState> = _state.asStateFlow()

    init {
        getContacts()
    }

    private fun getContacts() {
        viewModelScope.launch {

            repository.getContacts().collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is NetworkResult.Success -> {
                        val dataSize = result.data?.size ?: 0

                        _state.update {
                            it.copy(
                                isLoading = false,
                                contacts = result.data ?: emptyList(),
                                error = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.e("ContactsDebug", "ViewModel:Hata Mesajı: ${result.message}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
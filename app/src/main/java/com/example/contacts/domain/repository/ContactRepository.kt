package com.example.contacts.domain.repository

import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.model.NewContact
import com.example.contacts.domain.model.UpdateContact
import com.example.contacts.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ContactRepository {

    // If the DB changes, the UI changes. That's why Flow used.
    fun getContacts(): Flow<NetworkResult<List<Contact>>>

    // Fetches a single contact details using id.
    suspend fun getContactById(id: String): NetworkResult<Contact>

    // Creates new contact with required fields.
    suspend fun addContact(newContact: NewContact): NetworkResult<Unit>

    // Updates existing contact info.
    suspend fun updateContact(
        updateContact: UpdateContact
    ): NetworkResult<Unit>

    // Deletes the contact from both Server and Local DB.
    suspend fun deleteContact(id: String): NetworkResult<Unit>

    // Uploads the image file and takes the URL string.
    suspend fun uploadImage(imageFile: File): NetworkResult<String>
}
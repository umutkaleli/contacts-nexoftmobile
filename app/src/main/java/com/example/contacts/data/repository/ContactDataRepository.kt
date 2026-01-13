package com.example.contacts.data.repository

import android.util.Log
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.mapper.toCreateUserRequest
import com.example.contacts.data.mapper.toDomain
import com.example.contacts.data.mapper.toEntity
import com.example.contacts.data.mapper.toUpdateUserRequest
import com.example.contacts.data.remote.UserApi
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.model.NewContact
import com.example.contacts.domain.model.UpdateContact
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class ContactDataRepository(
    private val api: UserApi,
    private val dao: ContactDao
) : ContactRepository {

    // Get All Contacts
    override fun getContacts(): Flow<NetworkResult<List<Contact>>> = flow {
        emit(NetworkResult.Loading())
        // Sync remote
        try {
            val response = api.getUsers()


            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                val remoteUsers = body.data?.users ?: emptyList()

                if (remoteUsers.isNotEmpty()) {
                    // Refreshing cache for not allowing conflicts
                    dao.clearAll()
                    dao.insertContacts(remoteUsers.map { it.toEntity() })

                } else {
                    Log.w("RepoDebug", "Attention: Users list is empty.")
                }
            } else {
                Log.e("RepoDebug", "Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("RepoDebug", "CRASH: ${e.message}")
            e.printStackTrace()
        }
        dao.getContacts().collect { entities ->

            if (entities.isEmpty()) {
                emit(NetworkResult.Error("No contacts found!"))
            } else {
                emit(NetworkResult.Success(entities.map { it.toDomain() }))
            }
        }
    }

    // Get Contact By Id
    override suspend fun getContactById(id: String): NetworkResult<Contact> {
        // First checking DB
        val local = dao.getContactById(id)
        if (local != null) return NetworkResult.Success(local.toDomain())

        // İf not in DB checking remote
        return try {
            val response = api.getUserById(id)
            val user = response.body()?.data
            if (response.isSuccessful && user != null) {

                dao.insertContact(user.toEntity())
                NetworkResult.Success(user.toDomain())
            } else {
                NetworkResult.Error("User not found!")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error: ${e.localizedMessage}")
        }
    }

    // Add Contact
    override suspend fun addContact(newContact: NewContact): NetworkResult<Unit> {
        return try {
            val response = api.addUser(newContact.toCreateUserRequest())
            if (response.isSuccessful && response.body()?.success == true) {
                // Now, adding DB after adding remote backend
                val addedUser = response.body()?.data
                if (addedUser != null) {
                    dao.insertContact(addedUser.toEntity())
                }
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Failed to add contact: ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error: ${e.localizedMessage}")
        }
    }

    // Update Contact
    override suspend fun updateContact(updateContact: UpdateContact): NetworkResult<Unit> {
        return try {
            val response = api.updateUser(
                id = updateContact.id,
                request = updateContact.toUpdateUserRequest()
            )
            if (response.isSuccessful) {
                val updatedUser = response.body()?.data
                if (updatedUser != null) {
                    dao.insertContact(updatedUser.toEntity())
                }
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Update failed!")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error: ${e.localizedMessage}")
        }
    }

    // Delete Contact
    override suspend fun deleteContact(id: String): NetworkResult<Unit> {
        return try {
            val response = api.deleteUser(id)
            if (response.isSuccessful) {
                dao.deleteContact(id)
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("User could not deleted!")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error: ${e.localizedMessage}")
        }
    }

    override suspend fun uploadImage(imageFile: File): NetworkResult<String> {
        TODO("")
    }

}
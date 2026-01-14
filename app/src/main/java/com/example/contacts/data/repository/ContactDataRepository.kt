package com.example.contacts.data.repository

import android.util.Log
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.local.entity.ContactEntity
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.UUID

class ContactDataRepository(
    private val api: UserApi,
    private val dao: ContactDao
) : ContactRepository {

    // Get contacts with caching logic
    override fun getContacts(): Flow<NetworkResult<List<Contact>>> = flow {
        emit(NetworkResult.Loading())

        // First send local info to ui if there is
        try {
            val localCache = dao.getContactsOnce()
            if (localCache.isNotEmpty()) {
                emit(NetworkResult.Success(localCache.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            Log.e("RepoDebug", "Local fetch failed: ${e.message}")
        }

        // Sync with the remote server in background
        try {
            val response = api.getUsers()
            val body = response.body()

            if (response.isSuccessful && body?.success == true) {
                val remoteUsers = body.data?.users ?: emptyList()
                dao.clearAll()
                dao.insertContacts(remoteUsers.map { it.toEntity() })
            }
        } catch (e: Exception) {
            Log.e("RepoDebug", "Remote sync failed: ${e.message}")
        }

        // If DB changes send ui
        dao.getContacts().collect { entities ->
            emit(NetworkResult.Success(entities.map { it.toDomain() }))
        }
    }

    // Add Contact with caching
    override suspend fun addContact(newContact: NewContact): NetworkResult<Unit> {
        val tempId = UUID.randomUUID().toString()

        // First add local db to show in ui immediately
        val tempEntity = ContactEntity(
            id = tempId,
            firstName = newContact.firstName,
            lastName = newContact.lastName,
            phoneNumber = newContact.phoneNumber,
            profileImageUrl = newContact.profileImageUrl,
            isSavedInDevice = false
        )

        try {
            dao.insertContact(tempEntity)
        } catch (e: Exception) {
            return NetworkResult.Error("Local Save Failed")
        }

        // Send the data remote in background
        return try {
            val response = api.addUser(newContact.toCreateUserRequest())
            if (response.isSuccessful && response.body()?.success == true) {
                val remoteUser = response.body()?.data
                if (remoteUser != null) {
                    // On success: insert local with the remote id to sync
                    dao.deleteContact(tempId)
                    dao.insertContact(remoteUser.toEntity())
                }
                NetworkResult.Success(Unit)
            } else {
                dao.deleteContact(tempId)
                NetworkResult.Error("Server error: ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Success(Unit)
        }
    }

    // Get Contact / first look local
    override suspend fun getContactById(id: String): NetworkResult<Contact> {
        val local = dao.getContactById(id)
        if (local != null) return NetworkResult.Success(local.toDomain())

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

    // Update/first in local then remote
    override suspend fun updateContact(updateContact: UpdateContact): NetworkResult<Unit> {
        try {
            val currentLocal = dao.getContactById(updateContact.id)
            if (currentLocal != null) {
                val updatedLocal = currentLocal.copy(
                    firstName = updateContact.firstName,
                    lastName = updateContact.lastName,
                    phoneNumber = updateContact.phoneNumber,
                    profileImageUrl = updateContact.profileImageUrl
                )
                dao.insertContact(updatedLocal)
            }
        } catch (e: Exception) { Log.e("RepoDebug", "Local update failed") }

        return try {
            val response = api.updateUser(updateContact.id, updateContact.toUpdateUserRequest())
            if (response.isSuccessful) {
                val updatedUser = response.body()?.data
                if (updatedUser != null) dao.insertContact(updatedUser.toEntity())
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Remote update failed!")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Error: ${e.localizedMessage}")
        }
    }

    // Delete / first delete user from local then remote
    override suspend fun deleteContact(id: String): NetworkResult<Unit> {
        dao.deleteContact(id)

        return try {
            val response = api.deleteUser(id)
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Remote delete failed!")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network Error")
        }
    }

    // 6. Upload Image to remote
    override suspend fun uploadImage(imageFile: File): NetworkResult<String> {
        return try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
            val response = api.uploadImage(body)

            if (response.isSuccessful && response.body()?.success == true) {
                val url = response.body()?.data?.imageUrl ?: ""
                NetworkResult.Success(url)
            } else {
                NetworkResult.Error(response.message() ?: "Upload failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}
package com.example.contacts.data.mapper

import com.example.contacts.data.dto.CreateUserRequest
import com.example.contacts.data.dto.UpdateUserRequest
import com.example.contacts.data.dto.UserResponse
import com.example.contacts.data.local.entity.ContactEntity
import com.example.contacts.domain.model.Contact
import com.example.contacts.domain.model.NewContact
import com.example.contacts.domain.model.UpdateContact

//  API(UserResponse) -> DOMAIN (Contact)
fun UserResponse.toDomain(): Contact {
    return Contact(
        id = id ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        phoneNumber = phoneNumber ?: "",
        profileImageUrl = profileImageUrl,
        isInDevice = false
    )
}

//  DATABASE -> DOMAIN (Getting data from cache)
fun ContactEntity.toDomain(): Contact {
    return Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        isInDevice = isSavedInDevice == true
    )
}

//  API -> DATABASE (Using in cache)
fun UserResponse.toEntity(): ContactEntity {
    return ContactEntity(
        id = id ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        phoneNumber = phoneNumber ?: "",
        profileImageUrl = profileImageUrl,
        isSavedInDevice = true // DB'ye giriyorsa kayıtlıdır
    )
}


//  DOMAIN (NewContact) -> API (CreateUserRequest)
fun NewContact.toCreateUserRequest(): CreateUserRequest {
    return CreateUserRequest(
        firstName = this.firstName,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber,
        profileImageUrl = this.profileImageUrl
    )
}

//  DOMAIN (UpdateContact) -> API (UpdateUserRequest)
fun UpdateContact.toUpdateUserRequest(): UpdateUserRequest {
    return UpdateUserRequest(
        firstName = this.firstName,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber,
        profileImageUrl = this.profileImageUrl
    )
}
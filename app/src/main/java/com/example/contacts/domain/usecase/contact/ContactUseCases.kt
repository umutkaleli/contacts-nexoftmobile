package com.example.contacts.domain.usecase.contact


// This is a wrapper class to inject all contact related use cases into ViewModel at once.
data class ContactUseCases(
    val getContacts: GetContactsUseCase,
    val getContactById: GetContactByIdUseCase,
    val addContact: AddContactUseCase,
    val updateContact: UpdateContactUseCase,
    val deleteContact: DeleteContactUseCase,
    val uploadImage: UploadImageUseCase
)
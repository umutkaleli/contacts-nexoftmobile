package com.example.contacts.domain.usecase.contact

import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.NetworkResult
import java.io.File

class UploadImageUseCase(private val repository: ContactRepository) {
    suspend operator fun invoke(file: File): NetworkResult<String> {
        return repository.uploadImage(file)
    }
}
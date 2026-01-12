package com.example.contacts.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val profileImageUrl: String?,

    val isSavedInDevice: Boolean? = false
)
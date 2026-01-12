package com.example.contacts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.local.entity.ContactEntity

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
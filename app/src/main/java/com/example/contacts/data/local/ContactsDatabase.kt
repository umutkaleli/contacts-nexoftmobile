package com.example.contacts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.local.dao.SearchHistoryDao
import com.example.contacts.data.local.entity.ContactEntity
import com.example.contacts.data.local.entity.SearchHistoryEntity

@Database(
    entities = [ContactEntity::class, SearchHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    abstract fun searchHistoryDao(): SearchHistoryDao
}
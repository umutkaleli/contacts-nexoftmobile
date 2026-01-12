package com.example.contacts.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.contacts.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    //
    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: String): ContactEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    // Tekil ekleme/güncelleme
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    // ID'ye göre silme
    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContact(id: String)

    // Tüm tabloyu temizle (Örn: Kullanıcı çıkış yaparsa)
    @Query("DELETE FROM contacts")
    suspend fun clearAll()
}
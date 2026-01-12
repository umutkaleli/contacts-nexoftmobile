package com.example.contacts.di

import android.app.Application
import androidx.room.Room
import com.example.contacts.data.local.ContactsDatabase
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.remote.UserApi
import com.example.contacts.data.repository.ContactDataRepository
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppInstance {

    @Provides
    @Singleton
    fun provideContactsDatabase(app: Application): ContactsDatabase {
        return Room.databaseBuilder(
            app,
            ContactsDatabase::class.java,
            "contacts_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideContactDao(db: ContactsDatabase): ContactDao {
        return db.contactDao()
    }

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideContactRepository(
        api: UserApi,
        dao: ContactDao
    ): ContactRepository {
        return ContactDataRepository(api, dao)
    }
}
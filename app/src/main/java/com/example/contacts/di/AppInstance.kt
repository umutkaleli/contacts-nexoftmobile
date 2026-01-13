package com.example.contacts.di

import android.app.Application
import androidx.room.Room
import com.example.contacts.data.local.ContactsDatabase
import com.example.contacts.data.local.dao.ContactDao
import com.example.contacts.data.remote.UserApi
import com.example.contacts.data.repository.ContactDataRepository
import com.example.contacts.domain.repository.ContactRepository
import com.example.contacts.util.Constants
import com.example.contacts.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val newRequest = originalRequest.newBuilder()
                .addHeader("ApiKey", Constants.API_KEY)
                .build()

            chain.proceed(newRequest)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(client: OkHttpClient): UserApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
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
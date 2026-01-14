package com.example.contacts.data.manager

import android.content.ContentProviderOperation
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import android.database.Cursor

class DeviceContactsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun contactExistsFlow(phoneNumber: String?): Flow<Boolean> = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(checkIfContactExistsSync(phoneNumber))
            }
        }
        try {
            context.contentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        trySend(checkIfContactExistsSync(phoneNumber))

        awaitClose {
            context.contentResolver.unregisterContentObserver(observer)
        }
    }

    private fun checkIfContactExistsSync(phoneNumber: String?): Boolean {
        if (phoneNumber.isNullOrBlank()) return false
        return try {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
            val cursor = context.contentResolver.query(uri, arrayOf(ContactsContract.PhoneLookup._ID), null, null, null)
            val count = cursor?.use { it.count } ?: 0
            count > 0
        } catch (e: Exception) {
            false
        }
    }

    fun saveContactToDevice(firstName: String?, lastName: String?, phoneNumber: String?): Result<Unit> {
        return try {
            val ops = ArrayList<ContentProviderOperation>()
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build())

            if (!firstName.isNullOrEmpty()) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName).build())
            }
            if (!phoneNumber.isNullOrEmpty()) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build())
            }

            context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllDevicePhoneNumbers(): Set<String> {
        val phoneNumbers = mutableSetOf<String>()
        try {
            val cursor: Cursor? = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                null, null, null
            )

            cursor?.use {
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (it.moveToNext()) {
                    val rawNumber = it.getString(numberIndex)

                    val normalizedNumber = rawNumber?.replace(Regex("[^0-9]"), "")
                    if (!normalizedNumber.isNullOrBlank()) {
                        phoneNumbers.add(normalizedNumber)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return phoneNumbers
    }

}
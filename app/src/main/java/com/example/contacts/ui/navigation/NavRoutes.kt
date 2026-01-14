package com.example.contacts.ui.navigation

object NavRoutes {
    const val CONTACTS = "contacts_list"
    const val ADD_CONTACT = "add_contact"
    const val CONTACT_DETAIL = "contact_detail/{contactId}"
    const val EDIT_CONTACT = "edit_contact/{contactId}"

    fun contactDetail(id: String): String {
        return "contact_detail/$id"
    }
    fun editContact(id: String): String {
        return "edit_contact/$id"
    }
}
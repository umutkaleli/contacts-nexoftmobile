package com.example.contacts.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contacts.ui.screens.addcontact.AddContactSheet
import com.example.contacts.ui.screens.contactdetail.ContactDetailScreen
import com.example.contacts.ui.screens.contacts.ContactsScreen
import com.example.contacts.ui.screens.editcontact.EditContactSheet

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.CONTACTS
    ) { composable(route = NavRoutes.CONTACTS) {
            ContactsScreen(
                onAddClick = {
                    navController.navigate(NavRoutes.ADD_CONTACT)
                },
                onContactClick = { contactId ->
                    navController.navigate(NavRoutes.contactDetail(contactId))
                },
                onEditClick = { contactId ->
                    navController.navigate(NavRoutes.editContact(contactId))
                }
            )
        }
        composable(route = NavRoutes.ADD_CONTACT) {
            AddContactSheet(onDismiss = { navController.popBackStack() })
        }

        composable(
            route = NavRoutes.CONTACT_DETAIL,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) {
            ContactDetailScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { contactId -> navController.navigate(NavRoutes.editContact(contactId)) }
            )
        }

        composable(
            route = NavRoutes.EDIT_CONTACT,
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) {
            EditContactSheet(
                onDismiss = { navController.popBackStack() },
                onNavigateHome = {
                    navController.navigate(NavRoutes.CONTACTS) {
                        popUpTo(NavRoutes.CONTACTS) { inclusive = true }
                    }
                }
            )
        }
    }
}
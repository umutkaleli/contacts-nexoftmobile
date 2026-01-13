package com.example.contacts.ui.screens.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contacts.ui.components.ContactsHeader
import com.example.contacts.ui.components.ContactsSearchBar
import com.example.contacts.ui.components.NoContactsSection
import com.example.contacts.ui.theme.BackgroundGray
import com.example.contacts.ui.theme.ContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit
) {

    val isListEmpty = true

    Scaffold(
        containerColor = BackgroundGray,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            ContactsHeader(
                onAddClick = onAddClick
            )

            Spacer(modifier = Modifier.height(20.dp))


            ContactsSearchBar(
                onSearchClick = onSearchClick
            )


            Spacer(modifier = Modifier.weight(1f))


            if (isListEmpty) {
                NoContactsSection(
                    onCreateClick = onAddClick
                )
            } else {
                // TODO: Kişi Listesi buraya gelecek
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactsScreenPreview() {

    ContactsTheme {
        ContactsScreen(
            onAddClick = {},
            onSearchClick = {}
        )
    }
}
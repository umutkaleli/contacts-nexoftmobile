package com.example.contacts.ui.screens.contacts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contacts.ui.components.ContactItem
import com.example.contacts.ui.components.ContactsHeader
import com.example.contacts.ui.components.ContactsSearchBar
import com.example.contacts.ui.components.NoContactsSection
import com.example.contacts.ui.theme.BackgroundGray

@Composable
fun ContactsScreen(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = BackgroundGray,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    state.error != null -> {
                        Text(text = "Error: ${state.error}", modifier = Modifier.align(Alignment.Center))
                    }
                    state.contacts.isEmpty() -> {
                        NoContactsSection(
                            onCreateClick = onAddClick,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(state.contacts) { contact ->
                                ContactItem(
                                    contact = contact,
                                    onClick = { id ->
                                        // TODO: Go Detail Screen
                                        println("ID: $id")
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
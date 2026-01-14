package com.example.contacts.ui.screens.contacts

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.contacts.ui.components.*
import com.example.contacts.ui.theme.BackgroundGray

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactsScreen(
    onAddClick: () -> Unit,
    onContactClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val focusManager = LocalFocusManager.current

    var revealedContactId by remember { mutableStateOf<String?>(null) }
    val groupedContacts = remember(state.contacts) {
        state.contacts.groupBy { contact ->
            contact.firstName?.firstOrNull()?.uppercaseChar() ?: '#'
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.onEvent(ContactsEvent.CheckDeviceContacts)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
    BackHandler(enabled = state.isSearchActive) {
        focusManager.clearFocus()
        viewModel.onEvent(ContactsEvent.OnSearchQueryChanged(""))
        viewModel.onEvent(ContactsEvent.OnSearchFocusChange(false))
    }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ContactsUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ContactsTopBar(
                searchQuery = state.searchQuery,
                onSearchQueryChanged = { query -> viewModel.onEvent(ContactsEvent.OnSearchQueryChanged(query)) },
                onSearchFocusChanged = { isFocused -> viewModel.onEvent(ContactsEvent.OnSearchFocusChange(isFocused)) },
                onAddClick = onAddClick,
                onSearchTriggered = { query ->
                    viewModel.saveSearchToHistory(query)
                    focusManager.clearFocus()
                }
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }

                state.isSearchActive && state.searchQuery.isBlank() -> {
                    SearchHistoryView(
                        history = state.searchHistory,
                        onItemClick = { query ->
                            viewModel.onEvent(ContactsEvent.OnSearchHistoryItemClick(query))
                            focusManager.clearFocus()
                        },
                        onClearAll = { viewModel.onEvent(ContactsEvent.OnClearSearchHistory) },
                        onDeleteItem = { query -> viewModel.onEvent(ContactsEvent.OnDeleteSearchHistoryItem(query)) }
                    )
                }

                state.contacts.isEmpty() && state.searchQuery.isBlank() -> {
                    NoContactsView(onAddClick = onAddClick)
                }

                state.isNoResults -> {
                    NoResultsView()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        groupedContacts.forEach { (initial, contactsForInitial) ->
                            item {
                                CharacterHeader(char = initial)
                            }

                            itemsIndexed(
                                items = contactsForInitial,
                                key = { _, contact -> contact.id }
                            ) { index, contact ->

                                val itemShape = if (index == contactsForInitial.lastIndex) {
                                    RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                                } else {
                                    RectangleShape
                                }

                                Column {
                                    SwipeableItemWithActions(
                                        isRevealed = revealedContactId == contact.id,
                                        onReveal = { isOpen -> revealedContactId = if (isOpen) contact.id else null },
                                        shape = itemShape,
                                        actions = {
                                            ContactSwipeBackground(
                                                onEditClick = {
                                                    onEditClick(contact.id)
                                                    revealedContactId = null
                                                },
                                                onDeleteClick = {
                                                    viewModel.onEvent(ContactsEvent.OnDeleteContactClick(contact))
                                                    revealedContactId = null
                                                }
                                            )
                                        },
                                        content = {
                                            Box(modifier = Modifier.fillMaxWidth().background(Color.White, itemShape)) {
                                                ContactItem(
                                                    contact = contact,
                                                    onClick = {
                                                        if (revealedContactId == contact.id) {
                                                            revealedContactId = null
                                                        } else {
                                                            onContactClick(contact.id)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    if (index < contactsForInitial.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(start = 16.dp),
                                            color = BackgroundGray,
                                            thickness = 1.dp
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteSheet && state.contactToDelete != null) {
        DeleteConfirmationSheet(
            onConfirm = { viewModel.onEvent(ContactsEvent.OnConfirmDelete) },
            onCancel = { viewModel.onEvent(ContactsEvent.OnDismissDeleteSheet) },
            sheetState = sheetState
        )
    }
}
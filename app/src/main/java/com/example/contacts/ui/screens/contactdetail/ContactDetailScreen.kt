package com.example.contacts.ui.screens.contactdetail

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contacts.ui.components.ActionOutlinedButton
import com.example.contacts.ui.components.CustomToastMessage
import com.example.contacts.ui.components.DeleteConfirmationSheet
import com.example.contacts.ui.components.FigmaContactTextField
import com.example.contacts.ui.components.ProfilePictureSelector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: ContactDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LocalContext.current
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val deleteSheetState = rememberModalBottomSheetState()
    var isMenuExpanded by remember { mutableStateOf(false) }

    var showSuccessToast by remember { mutableStateOf(false) }
    var showDeleteToast by remember { mutableStateOf(false) }

    var showErrorToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var isSaveClicked by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSavedToDevice) {
        if (state.isSavedToDevice && isSaveClicked) {
            showSuccessToast = true
            isSaveClicked = false
            delay(3000)
            showSuccessToast = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) viewModel.onEvent(ContactDetailEvent.SaveToDevice)
        else {
            errorMessage = "Permission needed to save contact"
            showErrorToast = true
            scope.launch {
                delay(3000)
                showErrorToast = false
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            errorMessage = error
            showErrorToast = true
            delay(3000)
            showErrorToast = false
            viewModel.onEvent(ContactDetailEvent.DismissError)
        }
    }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            showDeleteToast = true
            delay(2000)
            showDeleteToast = false
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) onBackClick()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onBackClick() },
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.Black)
                        }

                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
                        ) {
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(180.dp),
                                offset = DpOffset(x = (-132).dp, y = 0.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Edit", fontSize = 16.sp, color = Color.Black) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = {
                                        isMenuExpanded = false
                                        state.contact?.id?.let { onEditClick(it) }
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.Black,
                                        trailingIconColor = Color.Black
                                    )
                                )

                                HorizontalDivider(thickness = 1.dp, color = Color(0xFFF0F0F0))

                                DropdownMenuItem(
                                    text = { Text("Delete", fontSize = 16.sp, color = Color.Red) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = null,
                                            tint = Color.Red,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = {
                                        isMenuExpanded = false
                                        viewModel.onEvent(ContactDetailEvent.DeleteMenuClicked)
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.Red,
                                        trailingIconColor = Color.Red
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ProfilePictureSelector(
                    imageUri = null,
                    imageUrl = state.contact?.profileImageUrl,
                    onClick = {
                        state.contact?.id?.let { onEditClick(it) }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                FigmaContactTextField(
                    value = state.contact?.firstName ?: "",
                    onValueChange = {},
                    placeholder = "First Name",
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                FigmaContactTextField(
                    value = state.contact?.lastName ?: "",
                    onValueChange = {},
                    placeholder = "Last Name",
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                FigmaContactTextField(
                    value = state.contact?.phoneNumber ?: "",
                    onValueChange = {},
                    placeholder = "Phone Number",
                    isPhone = true,
                    enabled = false
                )

                Spacer(modifier = Modifier.height(40.dp))

                ActionOutlinedButton(
                    text = if (state.isSavedToDevice) "Saved to Phone" else "Save to My Phone Contact",
                    icon = Icons.Outlined.BookmarkBorder,
                    isEnabled = !state.isSavedToDevice,
                    onClick = {
                        isSaveClicked = true
                        permissionLauncher.launch(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS))
                    }
                )

                if (state.isSavedToDevice) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("This contact is already saved your phone.", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                if (state.isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    AnimatedVisibility(
                        visible = showSuccessToast,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                    ) {
                        CustomToastMessage(
                            message = "User is added to your phone!"
                        )
                    }

                    AnimatedVisibility(
                        visible = showDeleteToast,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                    ) {
                        CustomToastMessage(
                            message = "User is deleted!"
                        )
                    }

                    AnimatedVisibility(
                        visible = showErrorToast,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                    ) {
                        CustomToastMessage(
                            message = errorMessage
                        )
                    }
                }
            }
        }
    }

    if (state.showDeleteSheet) {
        DeleteConfirmationSheet(
            onConfirm = { viewModel.onEvent(ContactDetailEvent.ConfirmDelete) },
            onCancel = { viewModel.onEvent(ContactDetailEvent.DismissDeleteSheet) },
            sheetState = deleteSheetState
        )
    }
}
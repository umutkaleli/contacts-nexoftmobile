package com.example.contacts.ui.screens.editcontact

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contacts.ui.components.CustomToastMessage
import com.example.contacts.ui.components.FigmaContactTextField
import com.example.contacts.ui.components.ImagesourceOptionComponent
import com.example.contacts.ui.components.ProfilePictureSelector
import com.example.contacts.ui.theme.BluePrimary
import com.example.contacts.util.createTempPictureUri
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactSheet(
    onDismiss: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: EditContactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showUpdateToast by remember { mutableStateOf(false) }

    var showErrorToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showImageSourceOption by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            showImageSourceOption = false
            if (uri != null) viewModel.onEvent(EditContactEvent.SelectedImage(uri))
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            showImageSourceOption = false
            if (success && tempCameraUri != null) {
                viewModel.onEvent(EditContactEvent.SelectedImage(tempCameraUri))
            }
        }
    )

    LaunchedEffect(state.isUpdatedSuccessfully) {
        if (state.isUpdatedSuccessfully) {
            showUpdateToast = true
            delay(2000)
            showUpdateToast = false
            onNavigateHome()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            errorMessage = it
            showErrorToast = true
            delay(3000)
            showErrorToast = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = BluePrimary, fontSize = 17.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Edit Contact", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { viewModel.onEvent(EditContactEvent.UpdateContact) },
                        enabled = !state.isLoading
                    ) {
                        Text(
                            "Done",
                            color = if (state.isLoading) Color.Gray else BluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Photo Area
                ProfilePictureSelector(
                    imageUri = state.newSelectedImageUri,
                    imageUrl = state.currentImageUrl,
                    onClick = { showImageSourceOption = true }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Text Fields
                FigmaContactTextField(
                    value = state.firstName,
                    onValueChange = { viewModel.onEvent(EditContactEvent.EnteredFirstName(it)) },
                    placeholder = "First Name"
                )
                Spacer(modifier = Modifier.height(16.dp))
                FigmaContactTextField(
                    value = state.lastName,
                    onValueChange = { viewModel.onEvent(EditContactEvent.EnteredLastName(it)) },
                    placeholder = "Last Name"
                )
                Spacer(modifier = Modifier.height(16.dp))
                FigmaContactTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onEvent(EditContactEvent.EnteredPhoneNumber(it)) },
                    placeholder = "Phone Number",
                    isPhone = true
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Color.White) }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    AnimatedVisibility(
                        visible = showUpdateToast,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                    ) {
                        CustomToastMessage(message = "User is updated!")
                    }

                    AnimatedVisibility(
                        visible = showErrorToast,
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
                    ) {
                        CustomToastMessage(message = errorMessage)
                    }
                }
            }
        }
    }

    if (showImageSourceOption) {
        ImagesourceOptionComponent(
            onDismiss = { showImageSourceOption = false },
            onCameraClick = {
                val uri = createTempPictureUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            },
            onGalleryClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}
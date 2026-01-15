package com.example.contacts.ui.screens.addcontact

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.contacts.ui.components.FigmaContactTextField
import com.example.contacts.ui.components.ImagesourceOptionComponent
import com.example.contacts.ui.components.ProfilePictureSelector
import com.example.contacts.ui.theme.BluePrimary
import com.example.contacts.util.createTempPictureUri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactSheet(
    onDismiss: () -> Unit,
    viewModel: AddContactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Sheet State
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Show Image State (Camera or Gallery)
    var showImageSourceOption by remember { mutableStateOf(false) }

    // Temp uri
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }


    // Launcher gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            showImageSourceOption = false // Menüyü kapat
            if (uri != null) {
                viewModel.onEvent(AddContactEvent.SelectedImage(uri))
            }
        }
    )

    // Launcher camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            showImageSourceOption = false
            if (success && tempCameraUri != null) {
                viewModel.onEvent(AddContactEvent.SelectedImage(tempCameraUri))
            }
        }
    )

    LaunchedEffect(state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
        }
    }

    // Change state to Success screen
    if (state.isSavedSuccessfully) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            DoneNewContactScreen(onAnimationFinished = { onDismiss() })
        }
    } else {
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
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                        Text("New Contact", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(
                            onClick = { viewModel.onEvent(AddContactEvent.SaveContact) },
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Profile Photo Selector
                    ProfilePictureSelector(
                        imageUri = state.selectedImageUri,
                        imageUrl = null,
                        onClick = { showImageSourceOption = true }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    FigmaContactTextField(
                        value = state.firstName,
                        onValueChange = { viewModel.onEvent(AddContactEvent.EnteredFirstName(it)) },
                        placeholder = "First Name"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FigmaContactTextField(
                        value = state.lastName,
                        onValueChange = { viewModel.onEvent(AddContactEvent.EnteredLastName(it)) },
                        placeholder = "Last Name"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FigmaContactTextField(
                        value = state.phoneNumber,
                        onValueChange = { viewModel.onEvent(AddContactEvent.EnteredPhoneNumber(it)) },
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
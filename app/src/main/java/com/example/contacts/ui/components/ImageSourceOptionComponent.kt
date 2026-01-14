package com.example.contacts.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contacts.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesourceOptionComponent(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Camera Button
            OptionButton(
                text = "Camera",
                icon = Icons.Outlined.PhotoCamera,
                onClick = onCameraClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gallery Button
            OptionButton(
                text = "Gallery",
                icon = Icons.Outlined.Image,
                onClick = onGalleryClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Cancel Button
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Cancel",
                    color = BluePrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Bottom Padding
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
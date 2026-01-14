package com.example.contacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.contacts.domain.model.Contact

@Composable
fun ContactItem(
    contact: Contact,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFF2F2F7)),
            contentAlignment = Alignment.Center
        ) {
            if (contact.profileImageUrl.isNullOrEmpty()) {
                Text(
                    text = contact.firstName?.take(1)?.uppercase() ?: "?",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            } else {
                AsyncImage(
                    model = contact.profileImageUrl,
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color.Black
                )
                if (contact.isInDevice) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "Saved in Device",
                        tint = Color(0xFF34C759),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            if (!contact.phoneNumber.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = contact.phoneNumber, color = Color.Gray, fontSize = 14.sp)
            }
        }

    }
}
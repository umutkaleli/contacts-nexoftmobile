package com.example.contacts.ui.components

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.contacts.ui.theme.BluePrimary

@Composable
fun ProfilePictureSelector(
    imageUri: Uri?,
    imageUrl: String? = null,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    var shadowColor by remember { mutableStateOf(Color.LightGray) }

    val modelToLoad = imageUri ?: if (imageUrl.isNullOrBlank()) null else imageUrl

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(modelToLoad)
            .allowHardware(false)
            .crossfade(true)
            .listener(object : ImageRequest.Listener {
                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    val bitmap = (result.drawable as BitmapDrawable).bitmap
                    Palette.from(bitmap).generate { palette ->
                        val bestSwatch = palette?.vibrantSwatch
                            ?: palette?.darkVibrantSwatch
                            ?: palette?.lightVibrantSwatch
                            ?: palette?.dominantSwatch

                        bestSwatch?.rgb?.let { colorValue ->
                            shadowColor = Color(colorValue)
                        }
                    }
                }
            })
            .build()
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            // Glowing Layer (İf there is a picture)
            if (modelToLoad != null) {
                Box(
                    modifier = Modifier
                        .size(146.dp)
                        .background(
                            brush = Brush.radialGradient(
                                0.0f to shadowColor.copy(alpha = 0.8f),
                                1.0f to Color.Transparent
                            ),
                            shape = CircleShape
                        )
                )
            }

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(if (modelToLoad != null) Color.White else Color(0xFFD1D1D1))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                if (modelToLoad != null) {
                    // İf image
                    Image(
                        painter = painter,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // İf not image
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Add Photo",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (modelToLoad != null) "Change Photo" else "Add Photo",
            color = BluePrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onClick() }
        )
    }
}
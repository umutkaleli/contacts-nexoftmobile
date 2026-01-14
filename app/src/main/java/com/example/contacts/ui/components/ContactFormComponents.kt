package com.example.contacts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contacts.ui.theme.BorderGray

@Composable
fun OptionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), // Tasarımdaki dolgun yükseklik
        shape = RoundedCornerShape(50), // Tam yuvarlak (Hap şekli)
        border = BorderStroke(1.dp, Color.Black), // Siyah Çerçeve
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black, // Yazı ve ikon rengi
            containerColor = Color.White
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun FigmaContactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPhone: Boolean = false,
    enabled: Boolean = true
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = 1.dp,
                // Eğer enabled değilse veya focus yoksa gri çerçeve
                color = if (isFocused && enabled) Color.Black else Color(0xFFE5E5E5),
                shape = RoundedCornerShape(8.dp)
            ),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        ),
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = if (isPhone) KeyboardOptions(keyboardType = KeyboardType.Phone) else KeyboardOptions.Default,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        }
    )
}
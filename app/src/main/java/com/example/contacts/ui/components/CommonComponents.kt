package com.example.contacts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contacts.ui.theme.BorderGray


@Composable
fun ReadOnlyTextField(
    value: String,
    label: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = BorderGray,
                disabledContainerColor = Color.White,
                disabledLabelColor = Color.Gray
            ),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
        )
    }
}

@Composable
fun ActionOutlinedButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    activeColor: Color = Color.Black,
    disabledColor: Color = Color.LightGray
) {
    val currentColor = if (isEnabled) activeColor else disabledColor

    OutlinedButton(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(1.dp, currentColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = currentColor,
            disabledContentColor = disabledColor,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = currentColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = currentColor
        )
    }
}
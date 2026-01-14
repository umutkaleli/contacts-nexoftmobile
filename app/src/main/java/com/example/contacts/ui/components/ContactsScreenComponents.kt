package com.example.contacts.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.contacts.ui.theme.BackgroundGray
import com.example.contacts.ui.theme.BluePrimary
import com.example.contacts.ui.theme.RedDestructive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.rounded.Person

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    onReveal: (Boolean) -> Unit,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
    shape: Shape
) {
    val density = LocalDensity.current
    val actionWidth = 140.dp
    val actionWidthPx = with(density) { actionWidth.toPx() }
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isRevealed) {
        if (isRevealed) {
            offsetX.animateTo(-actionWidthPx, tween(300))
        } else {
            offsetX.animateTo(0f, tween(300))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(actionWidth)
                .align(Alignment.CenterEnd)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = actions
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val newValue = (offsetX.value + delta).coerceIn(-actionWidthPx, 0f)
                            offsetX.snapTo(newValue)
                        }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val threshold = -actionWidthPx / 2
                        if (offsetX.value < threshold) {
                            onReveal(true)
                        } else {
                            onReveal(false)
                        }
                    }
                )
        ) {
            content()
        }
    }
}

@Composable
fun ContactSwipeBackground(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Edit Button
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(BluePrimary),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(RedDestructive),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    }
}

@Composable
fun CharacterHeader(title: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun NoResultsView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(96.dp).background(Color.Gray.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Outlined.SearchOff, contentDescription = null, tint = Color.White, modifier = Modifier.size(56.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "No Results", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The user you are looking for could not be found.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun NoContactsView(
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFD1D1D1), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Contacts",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Contacts you've added will appear here.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create New Contact",
            color = BluePrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onAddClick() }
                .padding(8.dp)
        )
    }
}
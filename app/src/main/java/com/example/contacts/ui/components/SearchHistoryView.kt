package com.example.contacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contacts.ui.theme.BackgroundGray
import com.example.contacts.ui.theme.BluePrimary

@Composable
fun SearchHistoryView(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onClearAll: () -> Unit,
    onDeleteItem: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "SEARCH HISTORY", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            if (history.isNotEmpty()) {
                Text(
                    text = "Clear All",
                    color = BluePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onClearAll() }
                )
            }
        }

        if (history.isEmpty()) {
            Text(text = "No recent searches", color = Color.Gray, modifier = Modifier.padding(top = 10.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                items(history) { query ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(query) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp).clickable { onDeleteItem(query) }
                            )
                            Text(text = query, fontSize = 16.sp, color = Color.Black)
                        }
                        HorizontalDivider(color = BackgroundGray, thickness = 1.dp, modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}
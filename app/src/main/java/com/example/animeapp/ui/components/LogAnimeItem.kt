package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animeapp.data.model.Anime

@Composable
fun LogAnimeItem(anime: Anime, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Column {
            AsyncImage(
                model = anime.image_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            Column(Modifier.padding(8.dp)) {
                Text(anime.title, maxLines = 1, style = MaterialTheme.typography.titleSmall)
                Text("⭐ ${anime.score}", style = MaterialTheme.typography.bodySmall)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, contentDescription = null, Modifier.size(20.dp)) }
                    IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.error) }
                }
            }
        }
    }
}
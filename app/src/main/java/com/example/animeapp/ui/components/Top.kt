package com.example.animeapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.animeapp.ui.theme.BowlSpacing

@Preview(showBackground = true)
@Composable
fun AnimeTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BowlSpacing.lg, vertical = BowlSpacing.lg)
    ) {
        Text(
            text = "Anime Bowl",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

package com.example.animeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun Navbar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 12.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavItem(
                activeIcon = Icons.Filled.Home,
                inactiveIcon = Icons.Outlined.Home,
                isSelected = currentRoute == "home",
                onClick = { onNavigate("home") }
            )

            NavItem(
                activeIcon = Icons.Filled.Search,
                inactiveIcon = Icons.Filled.Search,
                isSelected = false,
                onClick = onAddClick
            )

            NavItem(
                activeIcon = Icons.Filled.Person,
                inactiveIcon = Icons.Outlined.Person,
                isSelected = currentRoute == "profile",
                onClick = { onNavigate("profile") }
            )
        }
    }
}

@Composable
private fun NavItem(
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) activeIcon else inactiveIcon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}
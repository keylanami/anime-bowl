package com.example.animeapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.theme.BowlRadius
import com.example.animeapp.ui.theme.BowlSpacing

@Composable
fun Navbar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(BowlRadius.xl),
        shadowElevation = 10.dp,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BowlSpacing.lg, vertical = BowlSpacing.lg)
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = BowlSpacing.sm),
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
                onClick = onAddClick,
                isPrimary = true
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
    onClick: () -> Unit,
    isPrimary: Boolean = false
) {
    val iconColor by animateColorAsState(
        targetValue = when {
            isPrimary -> MaterialTheme.colorScheme.onPrimary
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "navIconColor"
    )
    val itemSize by animateDpAsState(
        targetValue = if (isSelected) 52.dp else 48.dp,
        label = "navSize"
    )

    if (isPrimary) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(52.dp)
        ) {
            Icon(activeIcon, contentDescription = null, modifier = Modifier.size(23.dp))
        }
        return
    }

    Box(
        modifier = Modifier
            .size(itemSize)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) activeIcon else inactiveIcon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

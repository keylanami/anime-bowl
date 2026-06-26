package com.example.animeapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = BowlPurpleSoft,
    onPrimary = BowlTextPrimary,
    primaryContainer = BowlPurple,
    onPrimaryContainer = BowlSurface,
    secondary = BowlPurpleMuted,
    background = BowlDarkBackground,
    surface = BowlDarkSurface,
    surfaceVariant = BowlDarkSurfaceSoft,
    onBackground = BowlSurface,
    onSurface = BowlSurface,
    onSurfaceVariant = Color(0xFFC8C4CF),
    outline = BowlDivider,
    error = BowlError
)

private val LightColorScheme = lightColorScheme(
    primary = BowlPurple,
    onPrimary = BowlSurface,
    primaryContainer = BowlPurpleSoft,
    onPrimaryContainer = BowlPurple,
    secondary = BowlPurpleMuted,
    background = BowlBackground,
    surface = BowlSurface,
    surfaceVariant = BowlSurfaceSoft,
    onBackground = BowlTextPrimary,
    onSurface = BowlTextPrimary,
    onSurfaceVariant = BowlTextSecondary,
    outline = BowlDivider,
    error = BowlError

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun AnimeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}

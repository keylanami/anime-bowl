package com.example.animeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.animeapp.R // Pastikan import ini sesuai dengan nama package project-mu

// 1. Deklarasi FontFamily untuk Jetbrains
val JetbrainsFamily = FontFamily(
    Font(R.font.jetbrains_regular, FontWeight.Normal),
    Font(R.font.jetbrains_semibold, FontWeight.SemiBold)
)

// 2. Terapkan ke Typography Material 3
val Typography = Typography(
    // Style untuk teks paragraf / deskripsi biasa
    bodyLarge = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Style untuk Judul (Appbar, Judul Item, dll) -> Pakai SemiBold
    titleLarge = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Style untuk Label (Tombol, Badge, dll)
    labelLarge = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = JetbrainsFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
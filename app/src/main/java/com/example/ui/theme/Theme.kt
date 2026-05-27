package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val BrandedDarkColorScheme =
  darkColorScheme(
    primary = BoldThemeAccentPurple,
    secondary = BoldThemeDeepPurple,
    tertiary = BoldThemePillBg,
    background = BoldThemeBackground,
    surface = BoldThemeBackground,
    surfaceVariant = BoldThemeCardBackground,
    error = CrimsonRed,
    onPrimary = BoldThemeDeepPurple,
    onSecondary = BoldThemeAccentPurple,
    onTertiary = BoldThemeTextDark,
    onBackground = BoldThemeTextLight,
    onSurface = BoldThemeTextLight,
    onSurfaceVariant = BoldThemeMutedText,
    onError = CleanWhite
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force beautiful branded dark theme for design portfolio
  dynamicColor: Boolean = false, // Use our signature theme for consistent branding
  content: @Composable () -> Unit,
) {
  val colorScheme = BrandedDarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

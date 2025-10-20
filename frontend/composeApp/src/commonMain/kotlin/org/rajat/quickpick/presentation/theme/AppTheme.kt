package org.rajat.quickpick.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.Light.OnPrimary,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.Light.OnSurface,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.Light.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.Light.OnSurface,
    tertiary = AppColors.Accent,
    onTertiary = AppColors.Light.OnSecondary,
    tertiaryContainer = AppColors.AccentContainer,
    onTertiaryContainer = AppColors.Light.OnSurface,
    error = AppColors.Error,
    onError = AppColors.Light.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.Light.OnSurface,
    background = AppColors.Light.Background,
    onBackground = AppColors.Light.OnBackground,
    surface = AppColors.Light.Surface,
    onSurface = AppColors.Light.OnSurface,
    surfaceVariant = AppColors.Light.SurfaceVariant,
    onSurfaceVariant = AppColors.Light.OnSurfaceVariant,
    surfaceContainer = AppColors.Light.SurfaceContainer,
    surfaceContainerHigh = AppColors.Light.SurfaceContainerHigh,
    surfaceContainerHighest = AppColors.Light.SurfaceVariant,
    surfaceContainerLow = AppColors.Light.SurfaceContainerLow,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    outline = AppColors.Light.Outline,
    outlineVariant = AppColors.Light.OutlineVariant,
    scrim = AppColors.Light.Scrim,
    inverseSurface = AppColors.Dark.Surface,
    inverseOnSurface = AppColors.Dark.OnSurface,
    inversePrimary = AppColors.PrimaryLight,
    surfaceTint = AppColors.Primary,
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = AppColors.Light.SurfaceVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PrimaryLight,
    onPrimary = AppColors.Dark.OnPrimary,
    primaryContainer = AppColors.PrimaryDark,
    onPrimaryContainer = AppColors.PrimaryContainer,
    secondary = AppColors.SecondaryLight,
    onSecondary = AppColors.Dark.OnSecondary,
    secondaryContainer = AppColors.SecondaryDark,
    onSecondaryContainer = AppColors.SecondaryContainer,
    tertiary = AppColors.AccentLight,
    onTertiary = AppColors.Dark.OnSecondary,
    tertiaryContainer = AppColors.AccentDark,
    onTertiaryContainer = AppColors.AccentContainer,
    error = AppColors.ErrorLight,
    onError = AppColors.Dark.OnError,
    errorContainer = AppColors.ErrorDark,
    onErrorContainer = AppColors.ErrorContainer,
    background = AppColors.Dark.Background,
    onBackground = AppColors.Dark.OnBackground,
    surface = AppColors.Dark.Surface,
    onSurface = AppColors.Dark.OnSurface,
    surfaceVariant = AppColors.Dark.SurfaceVariant,
    onSurfaceVariant = AppColors.Dark.OnSurfaceVariant,
    surfaceContainer = AppColors.Dark.SurfaceContainer,
    surfaceContainerHigh = AppColors.Dark.SurfaceContainerHigh,
    surfaceContainerHighest = AppColors.Dark.SurfaceVariant,
    surfaceContainerLow = AppColors.Dark.SurfaceContainerLow,
    surfaceContainerLowest = Color(0xFF080C0A),
    outline = AppColors.Dark.Outline,
    outlineVariant = AppColors.Dark.OutlineVariant,
    scrim = AppColors.Dark.Scrim,
    inverseSurface = AppColors.Light.Surface,
    inverseOnSurface = AppColors.Light.OnSurface,
    inversePrimary = AppColors.Primary,
    surfaceTint = AppColors.PrimaryLight,
    surfaceBright = AppColors.Dark.SurfaceVariant,
    surfaceDim = AppColors.Dark.Background
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
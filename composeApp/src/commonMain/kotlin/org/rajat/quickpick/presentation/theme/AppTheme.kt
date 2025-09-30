package org.rajat.quickpick.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.Light.OnPrimary,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.Light.OnSurface,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.Light.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.Light.OnSurface,
    tertiary = AppColors.PrimaryLight,
    onTertiary = AppColors.Light.OnPrimary,
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
    outline = AppColors.Light.Outline,
    outlineVariant = AppColors.Light.OutlineVariant,
    scrim = AppColors.Light.Scrim,
    inverseSurface = AppColors.Dark.Surface,
    inverseOnSurface = AppColors.Dark.OnSurface,
    inversePrimary = AppColors.PrimaryLight
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
    tertiary = AppColors.Primary,
    onTertiary = AppColors.Dark.OnPrimary,
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
    outline = AppColors.Dark.Outline,
    outlineVariant = AppColors.Dark.OutlineVariant,
    scrim = AppColors.Dark.Scrim,
    inverseSurface = AppColors.Light.Surface,
    inverseOnSurface = AppColors.Light.OnSurface,
    inversePrimary = AppColors.Primary
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
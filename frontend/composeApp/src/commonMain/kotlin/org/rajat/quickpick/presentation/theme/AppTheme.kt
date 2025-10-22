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
    onPrimaryContainer = AppColors.Light.OnPrimary,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.Light.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.Light.OnSecondary,
    tertiary = AppColors.Accent,
    onTertiary = AppColors.Light.OnSecondary,
    tertiaryContainer = AppColors.AccentContainer,
    onTertiaryContainer = AppColors.Light.OnSecondary,
    error = AppColors.Error,
    onError = AppColors.Light.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.Light.OnError,
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
    inversePrimary = AppColors.PrimaryLight,
    surfaceTint = AppColors.Primary
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PrimaryLight,
    onPrimary = AppColors.Dark.OnPrimary,
    primaryContainer = AppColors.PrimaryContainer,
    onPrimaryContainer = AppColors.Dark.OnPrimary,
    secondary = AppColors.SecondaryLight,
    onSecondary = AppColors.Dark.OnSecondary,
    secondaryContainer = AppColors.SecondaryContainer,
    onSecondaryContainer = AppColors.Dark.OnSecondary,
    tertiary = AppColors.AccentLight,
    onTertiary = AppColors.Dark.OnSecondary,
    tertiaryContainer = AppColors.AccentContainer,
    onTertiaryContainer = AppColors.Dark.OnSecondary,
    error = AppColors.Error,
    onError = AppColors.Dark.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.Dark.OnError,
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
    inversePrimary = AppColors.Primary,
    surfaceTint = AppColors.PrimaryLight
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
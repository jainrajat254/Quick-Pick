package org.rajat.quickpick.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
private fun getLightColorScheme(isVendor: Boolean) = lightColorScheme(
    primary = if (isVendor) VendorColors.Primary else StudentColors.Primary,
    onPrimary = if (isVendor) VendorColors.Light.OnPrimary else StudentColors.Light.OnPrimary,
    primaryContainer = if (isVendor) VendorColors.PrimaryContainer else StudentColors.PrimaryContainer,
    onPrimaryContainer = if (isVendor) VendorColors.Light.OnPrimary else StudentColors.Light.OnPrimary,
    secondary = if (isVendor) VendorColors.Secondary else StudentColors.Secondary,
    onSecondary = if (isVendor) VendorColors.Light.OnSecondary else StudentColors.Light.OnSecondary,
    secondaryContainer = if (isVendor) VendorColors.SecondaryContainer else StudentColors.SecondaryContainer,
    onSecondaryContainer = if (isVendor) VendorColors.Light.OnSecondary else StudentColors.Light.OnSecondary,
    tertiary = if (isVendor) VendorColors.Accent else StudentColors.Accent,
    onTertiary = if (isVendor) VendorColors.Light.OnSecondary else StudentColors.Light.OnSecondary,
    tertiaryContainer = if (isVendor) VendorColors.AccentContainer else StudentColors.AccentContainer,
    onTertiaryContainer = if (isVendor) VendorColors.Light.OnSecondary else StudentColors.Light.OnSecondary,
    error = if (isVendor) VendorColors.Error else StudentColors.Error,
    onError = if (isVendor) VendorColors.Light.OnError else StudentColors.Light.OnError,
    errorContainer = if (isVendor) VendorColors.ErrorContainer else StudentColors.ErrorContainer,
    onErrorContainer = if (isVendor) VendorColors.Light.OnError else StudentColors.Light.OnError,
    background = if (isVendor) VendorColors.Light.Background else StudentColors.Light.Background,
    onBackground = if (isVendor) VendorColors.Light.OnBackground else StudentColors.Light.OnBackground,
    surface = if (isVendor) VendorColors.Light.Surface else StudentColors.Light.Surface,
    onSurface = if (isVendor) VendorColors.Light.OnSurface else StudentColors.Light.OnSurface,
    surfaceVariant = if (isVendor) VendorColors.Light.SurfaceVariant else StudentColors.Light.SurfaceVariant,
    onSurfaceVariant = if (isVendor) VendorColors.Light.OnSurfaceVariant else StudentColors.Light.OnSurfaceVariant,
    outline = if (isVendor) VendorColors.Light.Outline else StudentColors.Light.Outline,
    outlineVariant = if (isVendor) VendorColors.Light.OutlineVariant else StudentColors.Light.OutlineVariant,
    scrim = if (isVendor) VendorColors.Light.Scrim else StudentColors.Light.Scrim,
    inverseSurface = if (isVendor) VendorColors.Dark.Surface else StudentColors.Dark.Surface,
    inverseOnSurface = if (isVendor) VendorColors.Dark.OnSurface else StudentColors.Dark.OnSurface,
    inversePrimary = if (isVendor) VendorColors.PrimaryLight else StudentColors.PrimaryLight,
    surfaceTint = if (isVendor) VendorColors.Primary else StudentColors.Primary
)

@Composable
private fun getDarkColorScheme(isVendor: Boolean) = darkColorScheme(
    primary = if (isVendor) VendorColors.PrimaryLight else StudentColors.PrimaryLight,
    onPrimary = if (isVendor) VendorColors.Dark.OnPrimary else StudentColors.Dark.OnPrimary,
    primaryContainer = if (isVendor) VendorColors.PrimaryContainer else StudentColors.PrimaryContainer,
    onPrimaryContainer = if (isVendor) VendorColors.Dark.OnPrimary else StudentColors.Dark.OnPrimary,
    secondary = if (isVendor) VendorColors.SecondaryLight else StudentColors.SecondaryLight,
    onSecondary = if (isVendor) VendorColors.Dark.OnSecondary else StudentColors.Dark.OnSecondary,
    secondaryContainer = if (isVendor) VendorColors.SecondaryContainer else StudentColors.SecondaryContainer,
    onSecondaryContainer = if (isVendor) VendorColors.Dark.OnSecondary else StudentColors.Dark.OnSecondary,
    tertiary = if (isVendor) VendorColors.AccentLight else StudentColors.AccentLight,
    onTertiary = if (isVendor) VendorColors.Dark.OnSecondary else StudentColors.Dark.OnSecondary,
    tertiaryContainer = if (isVendor) VendorColors.AccentContainer else StudentColors.AccentContainer,
    onTertiaryContainer = if (isVendor) VendorColors.Dark.OnSecondary else StudentColors.Dark.OnSecondary,
    error = if (isVendor) VendorColors.Error else StudentColors.Error,
    onError = if (isVendor) VendorColors.Dark.OnError else StudentColors.Dark.OnError,
    errorContainer = if (isVendor) VendorColors.ErrorContainer else StudentColors.ErrorContainer,
    onErrorContainer = if (isVendor) VendorColors.Dark.OnError else StudentColors.Dark.OnError,
    background = if (isVendor) VendorColors.Dark.Background else StudentColors.Dark.Background,
    onBackground = if (isVendor) VendorColors.Dark.OnBackground else StudentColors.Dark.OnBackground,
    surface = if (isVendor) VendorColors.Dark.Surface else StudentColors.Dark.Surface,
    onSurface = if (isVendor) VendorColors.Dark.OnSurface else StudentColors.Dark.OnSurface,
    surfaceVariant = if (isVendor) VendorColors.Dark.SurfaceVariant else StudentColors.Dark.SurfaceVariant,
    onSurfaceVariant = if (isVendor) VendorColors.Dark.OnSurfaceVariant else StudentColors.Dark.OnSurfaceVariant,
    outline = if (isVendor) VendorColors.Dark.Outline else StudentColors.Dark.Outline,
    outlineVariant = if (isVendor) VendorColors.Dark.OutlineVariant else StudentColors.Dark.OutlineVariant,
    scrim = if (isVendor) VendorColors.Dark.Scrim else StudentColors.Dark.Scrim,
    inverseSurface = if (isVendor) VendorColors.Light.Surface else StudentColors.Light.Surface,
    inverseOnSurface = if (isVendor) VendorColors.Light.OnSurface else StudentColors.Light.OnSurface,
    inversePrimary = if (isVendor) VendorColors.Primary else StudentColors.Primary,
    surfaceTint = if (isVendor) VendorColors.PrimaryLight else StudentColors.PrimaryLight
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isVendor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) getDarkColorScheme(isVendor) else getLightColorScheme(isVendor)

    CompositionLocalProvider(LocalIsVendor provides isVendor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
package org.rajat.quickpick.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalIsVendor = compositionLocalOf { false }

object StudentColors {
    val Primary = Color(0xFF4A7D5E)
    val PrimaryDark = Color(0xFF2F5A42)
    val PrimaryLight = Color(0xFF6B9B7F)
    val PrimaryContainer = Color(0xFF8BC3A3)

    val Secondary = Color(0xFF5D8A70)
    val SecondaryDark = Color(0xFF3E6150)
    val SecondaryLight = Color(0xFF7FA890)
    val SecondaryContainer = Color(0xFFD5E8DD)

    val Accent = Color(0xFF4A9D7D)
    val AccentDark = Color(0xFF2F7056)
    val AccentLight = Color(0xFF6BBAA0)
    val AccentContainer = Color(0xFFCCEFE3)

    val Tertiary = Color(0xFF5D7D67)
    val TertiaryDark = Color(0xFF3F5A49)
    val TertiaryLight = Color(0xFF7F9B88)
    val TertiaryContainer = Color(0xFFE0EDE6)

    val Error = Color(0xFFD93636)
    val ErrorContainer = Color(0xFFFFDAD6)

    val Success = Color(0xFF4A9D7D)
    val SuccessContainer = Color(0xFFCCEFE3)

    val Warning = Color(0xFFE6A23C)
    val WarningContainer = Color(0xFFFFF4E5)

    val BrightApple = Color(0xFF5D9D6A)
    val BrightAppleContainer = Color(0xFFD9F2DD)

    val SunnyYellow = Color(0xFFF5C842)
    val SunnyYellowContainer = Color(0xFFFFF8E1)

    val FreshMint = Color(0xFF4DBDA8)
    val FreshMintContainer = Color(0xFFD4F5EE)

    object Light {
        val Background = Color(0xFFF8FDFB)
        val Surface = Color(0xFFFFFFFF)
        val SurfaceVariant = Color(0xFFF0F7F3)
        val SurfaceContainer = Color(0xFFE8F3ED)
        val SurfaceContainerHigh = Color(0xFFDCEDE5)
        val SurfaceContainerLow = Color(0xFFF5FAF8)

        val OnPrimary = Color(0xFFFFFFFF)
        val OnSecondary = Color(0xFFFFFFFF)
        val OnTertiary = Color(0xFFFFFFFF)
        val OnBackground = Color(0xFF1A1C1A)
        val OnSurface = Color(0xFF1A1C1A)
        val OnSurfaceVariant = Color(0xFF3F4945)
        val OnError = Color(0xFFFFFFFF)

        val Outline = Color(0xFFC1D1C7)
        val OutlineVariant = Color(0xFFE0EDE6)
        val Scrim = Color(0xFF000000)
    }

    object Dark {
        val Background = Color(0xFF0E1512)
        val Surface = Color(0xFF1A2620)
        val SurfaceVariant = Color(0xFF273A30)
        val SurfaceContainer = Color(0xFF1F302A)
        val SurfaceContainerHigh = Color(0xFF2A3F35)
        val SurfaceContainerLow = Color(0xFF151E1A)

        val OnPrimary = Color(0xFFFFFFFF)
        val OnSecondary = Color(0xFFFFFFFF)
        val OnTertiary = Color(0xFFFFFFFF)
        val OnBackground = Color(0xFFE6F2ED)
        val OnSurface = Color(0xFFE6F2ED)
        val OnSurfaceVariant = Color(0xFFBFD1C7)
        val OnError = Color(0xFFFFFFFF)

        val Outline = Color(0xFF6B9B7F)
        val OutlineVariant = Color(0xFF4E6B5D)
        val Scrim = Color(0xFF000000)
    }
}

object VendorColors {
    val Primary = Color(0xFF6B5B6E)
    val PrimaryDark = Color(0xFF4A3D4D)
    val PrimaryLight = Color(0xFF8A7A8E)
    val PrimaryContainer = Color(0xFFAA9AAD)

    val Secondary = Color(0xFF7A6A7D)
    val SecondaryDark = Color(0xFF5A4A5D)
    val SecondaryLight = Color(0xFF9A8A9D)
    val SecondaryContainer = Color(0xFFE3D8E5)

    val Accent = Color(0xFF8B6B8E)
    val AccentDark = Color(0xFF6A4A6D)
    val AccentLight = Color(0xFFAA8AAD)
    val AccentContainer = Color(0xFFE8D9EA)

    val Tertiary = Color(0xFF7A6A7D)
    val TertiaryDark = Color(0xFF5A4A5D)
    val TertiaryLight = Color(0xFF9A8A9D)
    val TertiaryContainer = Color(0xFFEAE0EC)

    val Error = Color(0xFFD93636)
    val ErrorContainer = Color(0xFFFFDAD6)

    val Success = Color(0xFF8B6B8E)
    val SuccessContainer = Color(0xFFE8D9EA)

    val Warning = Color(0xFFE6A23C)
    val WarningContainer = Color(0xFFFFF4E5)

    val BrightApple = Color(0xFF8A6B8D)
    val BrightAppleContainer = Color(0xFFEDE3EE)

    val SunnyYellow = Color(0xFFF5C842)
    val SunnyYellowContainer = Color(0xFFFFF8E1)

    val FreshMint = Color(0xFF9B7A9E)
    val FreshMintContainer = Color(0xFFF0E6F2)

    object Light {
        val Background = Color(0xFFFCF9FD)
        val Surface = Color(0xFFFFFFFF)
        val SurfaceVariant = Color(0xFFF5F0F6)
        val SurfaceContainer = Color(0xFFEEE8F0)
        val SurfaceContainerHigh = Color(0xFFE6DFE8)
        val SurfaceContainerLow = Color(0xFFFAF6FB)

        val OnPrimary = Color(0xFFFFFFFF)
        val OnSecondary = Color(0xFFFFFFFF)
        val OnTertiary = Color(0xFFFFFFFF)
        val OnBackground = Color(0xFF1C1A1D)
        val OnSurface = Color(0xFF1C1A1D)
        val OnSurfaceVariant = Color(0xFF4A454C)
        val OnError = Color(0xFFFFFFFF)

        val Outline = Color(0xFFD1C7D3)
        val OutlineVariant = Color(0xFFE6DFE8)
        val Scrim = Color(0xFF000000)
    }

    object Dark {
        val Background = Color(0xFF14101A)
        val Surface = Color(0xFF241E28)
        val SurfaceVariant = Color(0xFF342A38)
        val SurfaceContainer = Color(0xFF2C2430)
        val SurfaceContainerHigh = Color(0xFF3A2F3E)
        val SurfaceContainerLow = Color(0xFF1C161E)

        val OnPrimary = Color(0xFFFFFFFF)
        val OnSecondary = Color(0xFFFFFFFF)
        val OnTertiary = Color(0xFFFFFFFF)
        val OnBackground = Color(0xFFF0E6F2)
        val OnSurface = Color(0xFFF0E6F2)
        val OnSurfaceVariant = Color(0xFFD1C7D3)
        val OnError = Color(0xFFFFFFFF)

        val Outline = Color(0xFF8A7A8E)
        val OutlineVariant = Color(0xFF5A4A5D)
        val Scrim = Color(0xFF000000)
    }
}

object AppColors {
    val Primary: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Primary else StudentColors.Primary
    val PrimaryDark: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.PrimaryDark else StudentColors.PrimaryDark
    val PrimaryLight: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.PrimaryLight else StudentColors.PrimaryLight
    val PrimaryContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.PrimaryContainer else StudentColors.PrimaryContainer

    val Secondary: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Secondary else StudentColors.Secondary
    val SecondaryDark: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SecondaryDark else StudentColors.SecondaryDark
    val SecondaryLight: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SecondaryLight else StudentColors.SecondaryLight
    val SecondaryContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SecondaryContainer else StudentColors.SecondaryContainer

    val Accent: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Accent else StudentColors.Accent
    val AccentDark: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.AccentDark else StudentColors.AccentDark
    val AccentLight: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.AccentLight else StudentColors.AccentLight
    val AccentContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.AccentContainer else StudentColors.AccentContainer

    val Tertiary: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Tertiary else StudentColors.Tertiary
    val TertiaryDark: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.TertiaryDark else StudentColors.TertiaryDark
    val TertiaryLight: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.TertiaryLight else StudentColors.TertiaryLight
    val TertiaryContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.TertiaryContainer else StudentColors.TertiaryContainer

    val Error: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Error else StudentColors.Error
    val ErrorContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.ErrorContainer else StudentColors.ErrorContainer

    val Success: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Success else StudentColors.Success
    val SuccessContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SuccessContainer else StudentColors.SuccessContainer

    val Warning: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.Warning else StudentColors.Warning
    val WarningContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.WarningContainer else StudentColors.WarningContainer

    val BrightApple: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.BrightApple else StudentColors.BrightApple
    val BrightAppleContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.BrightAppleContainer else StudentColors.BrightAppleContainer

    val SunnyYellow: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SunnyYellow else StudentColors.SunnyYellow
    val SunnyYellowContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.SunnyYellowContainer else StudentColors.SunnyYellowContainer

    val FreshMint: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.FreshMint else StudentColors.FreshMint
    val FreshMintContainer: Color
        @Composable get() = if (LocalIsVendor.current) VendorColors.FreshMintContainer else StudentColors.FreshMintContainer

    object Light {
        val Background: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.Background else StudentColors.Light.Background
        val Surface: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.Surface else StudentColors.Light.Surface
        val SurfaceVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.SurfaceVariant else StudentColors.Light.SurfaceVariant
        val SurfaceContainer: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.SurfaceContainer else StudentColors.Light.SurfaceContainer
        val SurfaceContainerHigh: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.SurfaceContainerHigh else StudentColors.Light.SurfaceContainerHigh
        val SurfaceContainerLow: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.SurfaceContainerLow else StudentColors.Light.SurfaceContainerLow

        val OnPrimary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnPrimary else StudentColors.Light.OnPrimary
        val OnSecondary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnSecondary else StudentColors.Light.OnSecondary
        val OnTertiary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnTertiary else StudentColors.Light.OnTertiary
        val OnBackground: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnBackground else StudentColors.Light.OnBackground
        val OnSurface: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnSurface else StudentColors.Light.OnSurface
        val OnSurfaceVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnSurfaceVariant else StudentColors.Light.OnSurfaceVariant
        val OnError: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OnError else StudentColors.Light.OnError

        val Outline: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.Outline else StudentColors.Light.Outline
        val OutlineVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.OutlineVariant else StudentColors.Light.OutlineVariant
        val Scrim: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Light.Scrim else StudentColors.Light.Scrim
    }

    object Dark {
        val Background: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.Background else StudentColors.Dark.Background
        val Surface: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.Surface else StudentColors.Dark.Surface
        val SurfaceVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.SurfaceVariant else StudentColors.Dark.SurfaceVariant
        val SurfaceContainer: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.SurfaceContainer else StudentColors.Dark.SurfaceContainer
        val SurfaceContainerHigh: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.SurfaceContainerHigh else StudentColors.Dark.SurfaceContainerHigh
        val SurfaceContainerLow: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.SurfaceContainerLow else StudentColors.Dark.SurfaceContainerLow

        val OnPrimary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnPrimary else StudentColors.Dark.OnPrimary
        val OnSecondary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnSecondary else StudentColors.Dark.OnSecondary
        val OnTertiary: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnTertiary else StudentColors.Dark.OnTertiary
        val OnBackground: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnBackground else StudentColors.Dark.OnBackground
        val OnSurface: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnSurface else StudentColors.Dark.OnSurface
        val OnSurfaceVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnSurfaceVariant else StudentColors.Dark.OnSurfaceVariant
        val OnError: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OnError else StudentColors.Dark.OnError

        val Outline: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.Outline else StudentColors.Dark.Outline
        val OutlineVariant: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.OutlineVariant else StudentColors.Dark.OutlineVariant
        val Scrim: Color
            @Composable get() = if (LocalIsVendor.current) VendorColors.Dark.Scrim else StudentColors.Dark.Scrim
    }
}

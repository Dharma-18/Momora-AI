package com.momora.ai.core.theme

import androidx.compose.ui.graphics.Color

/**
 * Momora AI Color System
 * 
 * Derived from the HTML mockup's Material Design 3 color tokens.
 * Deep indigo/purple primary with electric cyan secondary and vivid green tertiary.
 */
object MomoraColors {
    // Primary
    val Primary = Color(0xFFC4C0FF)
    val OnPrimary = Color(0xFF2000A4)
    val PrimaryContainer = Color(0xFF8781FF)
    val OnPrimaryContainer = Color(0xFF1B0091)
    val PrimaryFixed = Color(0xFFE3DFFF)
    val PrimaryFixedDim = Color(0xFFC4C0FF)
    val OnPrimaryFixed = Color(0xFF100069)
    val OnPrimaryFixedVariant = Color(0xFF3622CA)
    val InversePrimary = Color(0xFF4F44E2)

    // Secondary
    val Secondary = Color(0xFFA2E7FF)
    val OnSecondary = Color(0xFF003642)
    val SecondaryContainer = Color(0xFF00D2FD)
    val OnSecondaryContainer = Color(0xFF005669)
    val SecondaryFixed = Color(0xFFB4EBFF)
    val SecondaryFixedDim = Color(0xFF3CD7FF)
    val OnSecondaryFixed = Color(0xFF001F27)
    val OnSecondaryFixedVariant = Color(0xFF004E5F)

    // Tertiary
    val Tertiary = Color(0xFF4DE082)
    val OnTertiary = Color(0xFF003919)
    val TertiaryContainer = Color(0xFF00A755)
    val OnTertiaryContainer = Color(0xFF003115)
    val TertiaryFixed = Color(0xFF6DFE9C)
    val TertiaryFixedDim = Color(0xFF4DE082)
    val OnTertiaryFixed = Color(0xFF00210C)
    val OnTertiaryFixedVariant = Color(0xFF005227)

    // Error
    val Error = Color(0xFFFFB4AB)
    val OnError = Color(0xFF690005)
    val ErrorContainer = Color(0xFF93000A)
    val OnErrorContainer = Color(0xFFFFDAD6)

    // Surface
    val Surface = Color(0xFF131315)
    val OnSurface = Color(0xFFE5E1E4)
    val SurfaceVariant = Color(0xFF353437)
    val OnSurfaceVariant = Color(0xFFC7C4D8)
    val SurfaceTint = Color(0xFFC4C0FF)
    val SurfaceDim = Color(0xFF131315)
    val SurfaceBright = Color(0xFF39393B)
    val SurfaceContainer = Color(0xFF201F22)
    val SurfaceContainerLow = Color(0xFF1C1B1D)
    val SurfaceContainerHigh = Color(0xFF2A2A2C)
    val SurfaceContainerHighest = Color(0xFF353437)
    val SurfaceContainerLowest = Color(0xFF0E0E10)
    val InverseSurface = Color(0xFFE5E1E4)
    val InverseOnSurface = Color(0xFF313032)

    // Background
    val Background = Color(0xFF131315)
    val OnBackground = Color(0xFFE5E1E4)

    // Outline
    val Outline = Color(0xFF918FA1)
    val OutlineVariant = Color(0xFF464555)

    // Gradients
    val GradientStart = Primary
    val GradientEnd = Secondary

    // Glass effects
    val GlassBackground = Color(0x66272728) // ~40% opacity #27272A
    val GlassBorder = Color(0x1AFFFFFF)     // ~10% opacity white
    val CardBackground = Color(0xFF27272A)

    // Special
    val WarningOrange = Color(0xFFFF9800)
}

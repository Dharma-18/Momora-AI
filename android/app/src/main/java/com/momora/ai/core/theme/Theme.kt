package com.momora.ai.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Momora AI Material 3 Dark Theme
 *
 * Deep indigo/purple primary with electric cyan secondary,
 * matching the premium glassmorphism aesthetic from the HTML mockups.
 */
private val MomoraDarkColorScheme = darkColorScheme(
    primary = MomoraColors.Primary,
    onPrimary = MomoraColors.OnPrimary,
    primaryContainer = MomoraColors.PrimaryContainer,
    onPrimaryContainer = MomoraColors.OnPrimaryContainer,
    inversePrimary = MomoraColors.InversePrimary,
    secondary = MomoraColors.Secondary,
    onSecondary = MomoraColors.OnSecondary,
    secondaryContainer = MomoraColors.SecondaryContainer,
    onSecondaryContainer = MomoraColors.OnSecondaryContainer,
    tertiary = MomoraColors.Tertiary,
    onTertiary = MomoraColors.OnTertiary,
    tertiaryContainer = MomoraColors.TertiaryContainer,
    onTertiaryContainer = MomoraColors.OnTertiaryContainer,
    error = MomoraColors.Error,
    onError = MomoraColors.OnError,
    errorContainer = MomoraColors.ErrorContainer,
    onErrorContainer = MomoraColors.OnErrorContainer,
    background = MomoraColors.Background,
    onBackground = MomoraColors.OnBackground,
    surface = MomoraColors.Surface,
    onSurface = MomoraColors.OnSurface,
    surfaceVariant = MomoraColors.SurfaceVariant,
    onSurfaceVariant = MomoraColors.OnSurfaceVariant,
    surfaceTint = MomoraColors.SurfaceTint,
    inverseSurface = MomoraColors.InverseSurface,
    inverseOnSurface = MomoraColors.InverseOnSurface,
    outline = MomoraColors.Outline,
    outlineVariant = MomoraColors.OutlineVariant,
    surfaceBright = MomoraColors.SurfaceBright,
    surfaceContainer = MomoraColors.SurfaceContainer,
    surfaceContainerHigh = MomoraColors.SurfaceContainerHigh,
    surfaceContainerHighest = MomoraColors.SurfaceContainerHighest,
    surfaceContainerLow = MomoraColors.SurfaceContainerLow,
    surfaceContainerLowest = MomoraColors.SurfaceContainerLowest,
    surfaceDim = MomoraColors.SurfaceDim,
)

@Composable
fun MomoraTheme(
    darkTheme: Boolean = true, // Always dark - matching the HTML mockup aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = MomoraDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surfaceContainer.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MomoraTypography,
        content = content,
    )
}

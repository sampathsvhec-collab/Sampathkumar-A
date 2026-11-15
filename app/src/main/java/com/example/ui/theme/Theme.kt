package com.example.ui.theme

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
    primary = SafetyAmberBright,
    onPrimary = SlateNavyDeep,
    primaryContainer = SafetyAmberContainerDark,
    onPrimaryContainer = SafetyAmberContainerLight,
    secondary = ElectricCyanBright,
    onSecondary = SlateNavyDeep,
    secondaryContainer = ElectricCyanContainerDark,
    onSecondaryContainer = ElectricCyanContainerLight,
    tertiary = WhatsAppGreenBright,
    onTertiary = WhatsAppGreenOnDark,
    background = SlateNavyDeep,
    onBackground = TextLightPrimary,
    surface = SlateNavy,
    onSurface = TextLightPrimary,
    surfaceVariant = SlateNavyVariant,
    onSurfaceVariant = TextLightSecondary,
    surfaceContainerLowest = SlateNavyDeep,
    surfaceContainerLow = SlateNavy,
    surfaceContainer = SlateNavySurface,
    surfaceContainerHigh = SlateNavyVariant,
    outline = SlateBorderDark,
    outlineVariant = Color(0xFF233044),
    error = ErrorPink,
    onError = ErrorCrimsonDark,
    errorContainer = ErrorCrimsonDark,
    onErrorContainer = ErrorCrimsonLight
)

private val LightColorScheme = lightColorScheme(
    primary = SlateNavy,
    onPrimary = CrispWhite,
    primaryContainer = SafetyAmberContainerLight,
    onPrimaryContainer = Color(0xFF78350F),
    secondary = ElectricCyan,
    onSecondary = CrispWhite,
    secondaryContainer = ElectricCyanContainerLight,
    onSecondaryContainer = Color(0xFF075985),
    tertiary = WhatsAppGreen,
    onTertiary = CrispWhite,
    background = WarmAlabaster,
    onBackground = TextSlatePrimary,
    surface = CrispWhite,
    onSurface = TextSlatePrimary,
    surfaceVariant = SoftSlateSurface,
    onSurfaceVariant = TextSlateSecondary,
    surfaceContainerLowest = CrispWhite,
    surfaceContainerLow = WarmAlabaster,
    surfaceContainer = SoftSlateSurface,
    surfaceContainerHigh = Color(0xFFE2E8F0),
    outline = SlateBorderLight,
    outlineVariant = Color(0xFFE2E8F0),
    error = ErrorCrimson,
    onError = CrispWhite,
    errorContainer = ErrorCrimsonLight,
    onErrorContainer = Color(0xFF7F1D1D)
)

val SSServicesShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SSServicesTheme(
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
        typography = SSServicesTypography,
        shapes = SSServicesShapes,
        content = content
    )
}

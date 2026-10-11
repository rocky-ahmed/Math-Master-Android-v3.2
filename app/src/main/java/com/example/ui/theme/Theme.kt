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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class AppThemeMode(val titleBn: String) {
    SYSTEM("সিস্টেম ডিফল্ট"),
    LIGHT("লাইট মোড"),
    DARK("ডার্ক মোড")
}

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = SlatePrimary,
    onPrimary = SlateOnPrimary,
    primaryContainer = SlatePrimaryContainer,
    onPrimaryContainer = SlateOnPrimaryContainer,
    secondary = TealSecondary,
    onSecondary = TealOnSecondary,
    secondaryContainer = TealSecondaryContainer,
    onSecondaryContainer = TealOnSecondaryContainer,
    tertiary = AmberTertiary,
    onTertiary = AmberOnTertiary,
    tertiaryContainer = AmberTertiaryContainer,
    onTertiaryContainer = AmberOnTertiaryContainer,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

@Composable
fun MathMasterTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    globalConfig: com.example.ui.customization.GlobalCustomConfig? = null,
    dynamicColor: Boolean = false, // Keep branded math theme colors crisp across devices
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply user custom theme overrides if present
    val colorScheme = if (globalConfig != null) {
        val accentColor = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customAccentHex)
        val primaryColor = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customPrimaryActionHex)

        if (darkTheme) {
            val bg = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customDarkBackgroundHex)
            val surface = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customDarkCardSurfaceHex)
            val text = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customDarkTextColorHex)
            baseScheme.copy(
                primary = primaryColor ?: baseScheme.primary,
                secondary = accentColor ?: baseScheme.secondary,
                background = bg ?: baseScheme.background,
                surface = surface ?: baseScheme.surface,
                onBackground = text ?: baseScheme.onBackground,
                onSurface = text ?: baseScheme.onSurface
            )
        } else {
            val bg = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customLightBackgroundHex)
            val surface = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customLightCardSurfaceHex)
            val text = com.example.ui.customization.ColorUtils.parseHexColor(globalConfig.customLightTextColorHex)
            baseScheme.copy(
                primary = primaryColor ?: baseScheme.primary,
                secondary = accentColor ?: baseScheme.secondary,
                background = bg ?: baseScheme.background,
                surface = surface ?: baseScheme.surface,
                onBackground = text ?: baseScheme.onBackground,
                onSurface = text ?: baseScheme.onSurface
            )
        }
    } else {
        baseScheme
    }

    val baseWeight = when (globalConfig?.fontWeight) {
        com.example.ui.customization.CustomFontWeight.BOLD -> FontWeight.Bold
        com.example.ui.customization.CustomFontWeight.MEDIUM -> FontWeight.Medium
        com.example.ui.customization.CustomFontWeight.REGULAR -> FontWeight.Normal
        null -> FontWeight.Normal
    }
    val scaleFactor = ((globalConfig?.interfaceScalePercent ?: 100).toFloat() / 100f).coerceIn(0.8f, 1.3f)

    val typography = Typography.copy(
        displayLarge = Typography.displayLarge.copy(fontWeight = baseWeight, fontSize = (Typography.displayLarge.fontSize.value * scaleFactor).sp),
        displayMedium = Typography.displayMedium.copy(fontWeight = baseWeight, fontSize = (Typography.displayMedium.fontSize.value * scaleFactor).sp),
        displaySmall = Typography.displaySmall.copy(fontWeight = baseWeight, fontSize = (Typography.displaySmall.fontSize.value * scaleFactor).sp),
        headlineLarge = Typography.headlineLarge.copy(fontWeight = baseWeight, fontSize = (Typography.headlineLarge.fontSize.value * scaleFactor).sp),
        headlineMedium = Typography.headlineMedium.copy(fontWeight = baseWeight, fontSize = (Typography.headlineMedium.fontSize.value * scaleFactor).sp),
        headlineSmall = Typography.headlineSmall.copy(fontWeight = baseWeight, fontSize = (Typography.headlineSmall.fontSize.value * scaleFactor).sp),
        titleLarge = Typography.titleLarge.copy(fontWeight = baseWeight, fontSize = (globalConfig?.titleFontSizeSp?.toFloat() ?: (Typography.titleLarge.fontSize.value * scaleFactor)).sp),
        titleMedium = Typography.titleMedium.copy(fontWeight = baseWeight, fontSize = (Typography.titleMedium.fontSize.value * scaleFactor).sp),
        titleSmall = Typography.titleSmall.copy(fontWeight = baseWeight, fontSize = (Typography.titleSmall.fontSize.value * scaleFactor).sp),
        bodyLarge = Typography.bodyLarge.copy(fontWeight = baseWeight, fontSize = (Typography.bodyLarge.fontSize.value * scaleFactor).sp),
        bodyMedium = Typography.bodyMedium.copy(fontWeight = baseWeight, fontSize = (Typography.bodyMedium.fontSize.value * scaleFactor).sp),
        bodySmall = Typography.bodySmall.copy(fontWeight = baseWeight, fontSize = (globalConfig?.subtitleFontSizeSp?.toFloat() ?: (Typography.bodySmall.fontSize.value * scaleFactor)).sp),
        labelLarge = Typography.labelLarge.copy(fontWeight = baseWeight, fontSize = (Typography.labelLarge.fontSize.value * scaleFactor).sp),
        labelMedium = Typography.labelMedium.copy(fontWeight = baseWeight, fontSize = (Typography.labelMedium.fontSize.value * scaleFactor).sp),
        labelSmall = Typography.labelSmall.copy(fontWeight = baseWeight, fontSize = (Typography.labelSmall.fontSize.value * scaleFactor).sp)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

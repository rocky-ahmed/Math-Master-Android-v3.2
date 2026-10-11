package com.example.ui.customization

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Utility for parsing and validating hex color strings (e.g. #RRGGBB or #AARRGGBB).
 */
object ColorUtils {
    val PRESET_PALETTES = listOf(
        Color(0xFF1E3A8A) to "রয়েল ব্লু",
        Color(0xFF0F766E) to "ডিপ টিল",
        Color(0xFFB45309) to "অ্যাম্বার",
        Color(0xFF7C3AED) to "পার্পল",
        Color(0xFFE11D48) to "ক্রিমসন",
        Color(0xFF059669) to "এমেরাল্ড",
        Color(0xFFD97706) to "গোল্ডেন",
        Color(0xFF2563EB) to "ওশান ব্লু",
        Color(0xFF4F46E5) to "ইন্ডিগো",
        Color(0xFF0891B2) to "সায়ান",
        Color(0xFF1F2937) to "ডার্ক স্লেট",
        Color(0xFFDC2626) to "রেড"
    )

    fun parseHexColor(hex: String?): Color? {
        if (hex.isNullOrBlank()) return null
        return try {
            val cleanHex = hex.trim().removePrefix("#")
            val colorInt = when (cleanHex.length) {
                6 -> android.graphics.Color.parseColor("#$cleanHex")
                8 -> android.graphics.Color.parseColor("#$cleanHex")
                3 -> {
                    val expanded = "${cleanHex[0]}${cleanHex[0]}${cleanHex[1]}${cleanHex[1]}${cleanHex[2]}${cleanHex[2]}"
                    android.graphics.Color.parseColor("#$expanded")
                }
                else -> return null
            }
            Color(colorInt)
        } catch (_: Exception) {
            null
        }
    }

    fun toHex(color: Color): String {
        val red = (color.red * 255).toInt().coerceIn(0, 255)
        val green = (color.green * 255).toInt().coerceIn(0, 255)
        val blue = (color.blue * 255).toInt().coerceIn(0, 255)
        return String.format("#%02X%02X%02X", red, green, blue)
    }

    fun isValidHex(hex: String): Boolean {
        val clean = hex.trim().removePrefix("#")
        return (clean.length == 6 || clean.length == 8) && clean.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }
    }

    /**
     * Calculates luminance contrast ratio between foreground and background.
     * Returns true if readable contrast ratio >= 3.0:1.
     */
    fun hasAdequateContrast(fg: Color, bg: Color): Boolean {
        fun luminance(c: Color): Double {
            val r = c.red.toDouble()
            val g = c.green.toDouble()
            val b = c.blue.toDouble()
            return 0.2126 * r + 0.7152 * g + 0.0722 * b
        }
        val l1 = luminance(fg)
        val l2 = luminance(bg)
        val ratio = if (l1 > l2) (l1 + 0.05) / (l2 + 0.05) else (l2 + 0.05) / (l1 + 0.05)
        return ratio >= 3.0
    }
}

/**
 * Persistence layer for user customization preferences using SharedPreferences and Moshi.
 * Ensures zero disruption to Room Database and existing user bookmarks or quiz records.
 */
class CustomizationPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("math_customization_prefs", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val sectionMapType = Types.newParameterizedType(
        Map::class.java,
        String::class.java,
        SectionCustomConfig::class.java
    )
    private val sectionMapAdapter = moshi.adapter<Map<String, SectionCustomConfig>>(sectionMapType)
    private val globalAdapter = moshi.adapter(GlobalCustomConfig::class.java)

    fun loadCustomizationState(): AppCustomizationState {
        val globalJson = prefs.getString(KEY_GLOBAL, null)
        val globalConfig = if (!globalJson.isNullOrBlank()) {
            try {
                globalAdapter.fromJson(globalJson) ?: GlobalCustomConfig()
            } catch (_: Exception) {
                GlobalCustomConfig()
            }
        } else {
            GlobalCustomConfig()
        }

        val sectionsJson = prefs.getString(KEY_SECTIONS, null)
        val sectionMap = if (!sectionsJson.isNullOrBlank()) {
            try {
                sectionMapAdapter.fromJson(sectionsJson) ?: emptyMap()
            } catch (_: Exception) {
                emptyMap()
            }
        } else {
            emptyMap()
        }

        return AppCustomizationState(
            global = globalConfig,
            sections = sectionMap
        )
    }

    fun saveCustomizationState(state: AppCustomizationState) {
        prefs.edit()
            .putString(KEY_GLOBAL, globalAdapter.toJson(state.global))
            .putString(KEY_SECTIONS, sectionMapAdapter.toJson(state.sections))
            .apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_GLOBAL = "custom_global_config_v1"
        private const val KEY_SECTIONS = "custom_sections_config_v1"
    }
}

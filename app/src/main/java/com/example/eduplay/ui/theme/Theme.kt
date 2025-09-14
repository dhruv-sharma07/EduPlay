package com.example.eduplay.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrightBlueDark,
    secondary = SunnyYellowDark,
    tertiary = PlayfulCoralDark,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = Color.Black, // Text on primary color (dark theme)
    onSecondary = Color.Black, // Text on secondary color (dark theme)
    onTertiary = Color.Black, // Text on tertiary color (dark theme)
    onBackground = LightText,
    onSurface = LightText,
    error = PlayfulCoralDark, // Or a specific error color for dark theme
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = BrightBlue,
    secondary = SunnyYellow,
    tertiary = PlayfulCoral,
    background = LightCream,
    surface = LightCream,
    onPrimary = Color.White, // Text on primary color (light theme)
    onSecondary = DarkGrey,   // Text on secondary color (light theme)
    onTertiary = Color.White,  // Text on tertiary color (light theme)
    onBackground = DarkGrey,
    onSurface = DarkGrey,
    error = PlayfulCoral, // Or a specific error color for light theme
    onError = Color.White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun EduPlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ - you can disable if you prefer your own theme
    dynamicColor: Boolean = false, // Set to false to use the custom color scheme above
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
        typography = Typography,
        content = content
    )
}
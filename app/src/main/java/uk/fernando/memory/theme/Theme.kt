package uk.fernando.memory.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import uk.fernando.memory.config.AppConfig.SCREEN_HEIGHT

private val DarkColorScheme = darkColorScheme(
    primary = greenLight,
    background = dark,
    secondary = orange,
    surface = lightDark,
    outline = green
)

private val LightColorScheme = lightColorScheme(
    primary = greenDark,
    secondary = orange,
    surface = Color.White,
    background = whiteBackGround,
    outline = green
)

@Composable
fun MemoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme //darkTheme
        }
    }

    val typography = if (SCREEN_HEIGHT > 600) Typography else TypographySmallScreen

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}
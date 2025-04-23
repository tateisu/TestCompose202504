package jp.juggler.testCompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// アプリ固有の色
object App1Colors {
    val textBluePrimary = Color(0xFF0080FF)
}

private val AppColorDark = darkColorScheme(
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    // primary is red
    primary = Color(0xFF8e3e3e), onPrimary = Color(0xFFff0000),
    // secondary is green
    secondary = Color(0xFF3e8e3e), onSecondary = Color(0xFF00ff00),
    // tertiary is blue
    tertiary = Color(0xFF3e3e8e), onTertiary = Color(0xFF0000ff),
)

private val AppColorLight = lightColorScheme(
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),

    // primary is red
    primary = Color(0xFFffc3c3), onPrimary = Color(0xFFff0000),
    // secondary is green
    secondary = Color(0xFFc3ffc3), onSecondary = Color(0xFF00ff00),
    // tertiary is blue
    tertiary = Color(0xFFc3c3ff), onTertiary = Color(0xFF0000ff),

    // OnSurfaceVariant means disabled text color
    // onSurfaceVariant = Color(0x4c000000),

    /*
        Other default colors to override
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
    */
)

// Set of Material typography styles to start with
val App1Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

@Composable
fun App1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // 色指定の確認の妨げになるので、 DynamicColorを無効にする
        //        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        //            val context = LocalContext.current
        //            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        //        }

        darkTheme -> AppColorDark
        else -> AppColorLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = App1Typography,
        content = content
    )
}

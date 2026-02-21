package project.repit.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary      = Purple,
    onPrimary    = Color.White,
    secondary    = Green,
    onSecondary  = Color.White,
    background   = BackgroundGray,
    surface      = CardWhite,
    onBackground = TextPrimary,
    onSurface    = TextPrimary,
)

@Composable
fun RepitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content,
    )
}
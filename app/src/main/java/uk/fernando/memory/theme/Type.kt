package uk.fernando.memory.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = solwayFamily,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = solwayFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = solwayFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = solwayFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = solwayFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    labelSmall = TextStyle(
        fontFamily = solwayFamily,
        fontSize = 10.sp
    ),
    labelMedium = TextStyle(
        fontFamily = solwayFamily,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = solwayFamily,
        fontSize = 18.sp
    ),
)
package uk.fernando.memory.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White,
    enabled: Boolean = true,
    fontSize: TextUnit = 17.sp,
    textModifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(4.dp, 0.dp)
) {
    Button(
        border = borderStroke,
        modifier = modifier.defaultMinSize(minHeight = 50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = elevation,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        contentPadding = contentPadding,
        onClick = onClick
    ) {
        Text(
            modifier = textModifier,
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }
}

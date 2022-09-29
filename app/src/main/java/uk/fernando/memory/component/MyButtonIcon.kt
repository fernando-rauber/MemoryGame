package uk.fernando.memory.component

import androidx.annotation.DrawableRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uk.fernando.util.event.MultipleEventsCutter
import uk.fernando.util.event.get

@Composable
fun MyButtonIcon(@DrawableRes icon: Int, onClick: () -> Unit) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        elevation = ButtonDefaults.buttonElevation(4.dp, 0.dp)
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}
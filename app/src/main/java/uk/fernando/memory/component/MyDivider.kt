package uk.fernando.memory.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.fernando.memory.theme.greyLight

@Composable
fun MyDivider(modifier: Modifier = Modifier.padding(vertical = 8.dp)) {
    Divider(
        modifier = modifier,
        color = greyLight.copy(0.3f)
    )
}
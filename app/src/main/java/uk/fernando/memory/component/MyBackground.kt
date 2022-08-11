package uk.fernando.memory.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyBackground(content: @Composable () -> Unit) {
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.primary,
            content = {}
        )

        content()
    }
}
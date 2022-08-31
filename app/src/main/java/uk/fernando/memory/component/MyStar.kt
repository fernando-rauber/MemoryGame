package uk.fernando.memory.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import uk.fernando.memory.R
import uk.fernando.memory.theme.dark
import uk.fernando.memory.theme.greyDark

@Composable
fun MyStar(modifier: Modifier, isBlack: Boolean = false) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.img_star),
        contentDescription = null,
        colorFilter = if (isBlack) ColorFilter.tint(dark) else null
    )
}
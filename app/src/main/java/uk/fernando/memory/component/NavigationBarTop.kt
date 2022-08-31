package uk.fernando.memory.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.fernando.memory.R

@Composable
fun NavigationBarTop(
    @StringRes title: Int?,
    onLeftIconClick: (() -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null
) {

    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        if (onLeftIconClick != null)
            IconButton(onClick = onLeftIconClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        if (title != null)
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(title),
                color =  MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )

        if (rightIcon != null) {
            Box(Modifier.align(Alignment.CenterEnd)) {
                rightIcon()
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}
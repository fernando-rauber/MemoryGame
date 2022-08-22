package uk.fernando.memory.component

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.fernando.memory.R
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.timerFormat
import uk.fernando.memory.theme.gold
import uk.fernando.memory.theme.greenLight
import uk.fernando.memory.theme.grey
import uk.fernando.memory.theme.red
import java.util.*

@Composable
fun MyResultDialog(
    level: LevelEntity,
    isFail: Boolean = false,
    @StringRes leftButtonText: Int,
    @StringRes rightButtonText: Int,
    onLeftButton: () -> Unit,
    onRightButton: () -> Unit
) {
    MyDialog {
        Column(
            Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Title(isFail)

            // Level text
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = stringResource(id = R.string.level_args, "${level.position}"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Stars
            Stars(level.starCount)

            // Time
            Time(level.time)

            // Attempts
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = stringResource(id = R.string.attempt_args, "999"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Buttons
            Row(Modifier.padding(top = 32.dp, bottom = 8.dp)) {
                MyButton(
                    modifier = Modifier.weight(1f),
                    onClick = onLeftButton,
                    color = red,
                    text = stringResource(leftButtonText)
                )

                Spacer(Modifier.width(8.dp))

                MyButton(
                    modifier = Modifier.weight(1f),
                    onClick = onRightButton,
                    text = stringResource(rightButtonText)
                )
            }
        }
    }
}

@Composable
private fun Title(isFail: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)
            .offset(y = -(30).dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(50),
        border = BorderStroke(4.dp, greenLight)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(if (isFail) R.string.fail else R.string.completed).uppercase(Locale.ENGLISH),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Stars(stars: Int) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {

        Icon(
            Icons.Filled.Star,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .offset(20.dp),
            contentDescription = null,
            tint = gold
        )

        Icon(
            Icons.Filled.Star,
            modifier = Modifier
                .weight(1.4f)
                .aspectRatio(1f),
            contentDescription = null,
            tint = if (stars > 1) gold else grey
        )

        Icon(
            Icons.Filled.Star,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .offset(-(20).dp),
            contentDescription = null,
            tint = if (stars > 2) gold else grey
        )
    }
}

@Composable
private fun Time(time: Int) {
    Row(
        modifier = Modifier.padding(start = 4.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.ic_timer),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = time.timerFormat(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


package uk.fernando.memory.component

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
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
import uk.fernando.memory.theme.gold
import uk.fernando.memory.theme.greenLight
import uk.fernando.memory.theme.grey
import uk.fernando.memory.theme.red
import java.util.*

@Composable
fun MyResultDialog(
    level: LevelEntity,
    @StringRes leftButtonText: Int,
    @StringRes rightButtonText: Int,
    onClose: () -> Unit,
    onLeftButton: () -> Unit,
    onRightButton: () -> Unit
) {
    MyDialog {
        Box {

            Column(
                Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Title(level.star)

                // Level text
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(id = R.string.level_args, "${level.position}"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Stars
                Stars(level.star)

                // Mistakes
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = stringResource(id = R.string.mistakes_dialog_args, level.mistakes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Buttons
                Row(Modifier.padding(top = 32.dp, bottom = 16.dp)) {
                    MyButton(
                        modifier = Modifier.weight(1f),
                        onClick = onLeftButton,
                        color = red,
                        text = stringResource(leftButtonText)
                    )

                    Spacer(Modifier.width(16.dp))

                    MyButton(
                        modifier = Modifier.weight(1f),
                        onClick = onRightButton,
                        text = stringResource(rightButtonText)
                    )
                }
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onClose
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun Title(stars: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(60.dp)
            .offset(y = -(30).dp),
        color = if (stars > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(50),
        border = BorderStroke(4.dp, if (stars > 0) greenLight else MaterialTheme.colorScheme.error.copy(0.8f))
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(if (stars == 0) R.string.fail else R.string.completed).uppercase(Locale.ENGLISH),
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
            tint = if (stars > 0) gold else grey
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
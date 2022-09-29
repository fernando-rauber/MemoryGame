package uk.fernando.memory.component

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.fernando.memory.R
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.theme.red
import uk.fernando.util.component.MyButton
import uk.fernando.util.component.MyDialog
import uk.fernando.util.component.MyIconButton
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
                    modifier = Modifier
                        .offset(y = -(10).dp)
                        .padding(bottom = 10.dp),
                    text = stringResource(id = R.string.level_args, "${level.id}"),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Stars
                Stars(level.star)

                // Mistakes
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = stringResource(id = R.string.mistakes_dialog_args, level.mistakes),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Buttons
                Row(Modifier.padding(vertical = 16.dp)) {
                    MyButton(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 50.dp),
                        onClick = onLeftButton,
                        color = MaterialTheme.colorScheme.surface,
                        text = stringResource(leftButtonText)
                    )

                    Spacer(Modifier.width(16.dp))

                    MyButton(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 50.dp),
                        onClick = onRightButton,
                        text = stringResource(rightButtonText)
                    )
                }
            }

            MyIconButton(
                icon = R.drawable.ic_close,
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onClose
            )
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
        color = if (stars > 0) MaterialTheme.colorScheme.primary else red,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(50),
        border = BorderStroke(2.dp, Color.White.copy(0.2f))
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(if (stars == 0) R.string.failed else R.string.completed).uppercase(Locale.ENGLISH),
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

        MyStar(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .offset(12.dp),
            isBlack = stars <= 0
        )

        MyStar(
            modifier = Modifier
                .weight(1.4f)
                .aspectRatio(1f),
            isBlack = stars <= 1
        )

        MyStar(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .offset((-12).dp),
            isBlack = stars <= 2
        )
    }
}
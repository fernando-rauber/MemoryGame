package uk.fernando.memory.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.R
import uk.fernando.memory.component.MyFlipCard
import uk.fernando.memory.ext.timerFormat
import uk.fernando.memory.theme.green
import uk.fernando.memory.viewmodel.GameViewModel

@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    levelId: Int,
    viewModel: GameViewModel = getViewModel()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            viewModel = viewModel,
            levelId = levelId,
            onClose = { navController.popBackStack() }
        )

        CardList(viewModel)
    }
}

@Composable
private fun TopBar(viewModel: GameViewModel, levelId: Int, onClose: () -> Unit) {
    Box(
        Modifier
            .padding(16.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {

        TopBarItemCard(Alignment.CenterStart) {
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
                    text = viewModel.chronometerSeconds.value.timerFormat(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        TopBarItemCard(Alignment.CenterEnd) {
            IconButton(
                onClick = onClose
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        TopBarItemCard(Alignment.Center) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.level_args, levelId.toString()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun BoxScope.TopBarItemCard(alignment: Alignment, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .align(alignment)
            .fillMaxHeight(),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}

@Composable
private fun CardList(viewModel: GameViewModel) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(4)
    ) {
        items(viewModel.cardList) { card ->

            var state by remember { mutableStateOf(card.status) }

            state = card.status

            MyFlipCard(
                cardFace = state,
                onClick = {
                    viewModel.setSelectedCard(card)
                },
                back = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(green),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${card.id}")
                    }
                }
            )
        }
    }
}

package uk.fernando.memory.screen.custom

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.R
import uk.fernando.memory.component.NavigationBarTop
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.ext.getCategoryIcon
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.orange
import uk.fernando.memory.viewmodel.custom.ScoreViewModel
import uk.fernando.util.component.MyButton
import uk.fernando.util.component.MyIconButton
import uk.fernando.util.ext.safeNav

@Composable
fun ScorePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: ScoreViewModel = getViewModel()
) {
    val scoreList = viewModel.score.collectAsLazyPagingItems()

    Box {

        Column {

            NavigationBarTop(
                title = R.string.score_title,
                rightIcon = {
                    MyIconButton(
                        icon = R.drawable.ic_settings,
                        onClick = { navController.safeNav(Directions.settings.path) },
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                })

            Box(Modifier.fillMaxSize()) {

                if (scoreList.loadState.refresh == LoadState.Loading)
                    MyLoadingHistory(Modifier.align(Alignment.Center))
                else {
                    if (scoreList.itemCount == 0)
                        MyEmptyHistory(
                            modifier = Modifier.fillMaxSize(),
                            message = R.string.empty_history_text,
                            onClick = { navController.safeNav(Directions.createGame.path) }
                        )
                    else
                        HistoryList(
                            modifier = Modifier.fillMaxSize(),
                            scoreList = scoreList
                        )
                }

//                MyAdBanner(Modifier.align(Alignment.BottomCenter), unitId = R.string.ad_banner_solo)
            }
        }

        if (scoreList.itemCount != 0)
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                containerColor = orange,
                onClick = { navController.safeNav(Directions.createGame.path) },
                icon = {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "New game",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.new_game_action),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
    }
}

@Composable
private fun HistoryList(modifier: Modifier, scoreList: LazyPagingItems<ScoreEntity>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
        modifier = modifier
    ) {

        items(scoreList) { score ->
            score?.let {
                HistoryCard(score)
            }
        }
    }
}

@Composable
private fun MyLoadingHistory(modifier: Modifier) {
    CircularProgressIndicator(
        strokeWidth = 5.dp,
        modifier = modifier
            .offset(y = (-70).dp)
            .fillMaxWidth(0.2f)
    )
}

@Composable
private fun MyEmptyHistory(modifier: Modifier, @StringRes message: Int, onClick: () -> Unit) {
    Box(modifier, contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 15.dp),
                text = stringResource(message),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            MyButton(
                modifier = Modifier
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClick,
                color = orange,
                shape = ButtonDefaults.shape,
                text = stringResource(R.string.start_new_game_action)
            )
        }
    }
}

@Composable
private fun HistoryCard(score: ScoreEntity) {

    Surface(
        modifier = Modifier.padding(bottom = 10.dp),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Column() {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.board_size_args, score.boardSize, score.boardSize),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Divider(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                Text(
                    modifier = Modifier.weight(1f).padding(  10.dp),
                    text = stringResource(R.string.attempts_args, score.attempts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Divider()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.categories).plus(":"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                score.categoryList.forEach { category ->
                    Image(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(18.dp),
                        painter = painterResource(category.getCategoryIcon()),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
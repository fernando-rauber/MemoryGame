package uk.fernando.memory.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.R
import uk.fernando.memory.component.MyAnimation
import uk.fernando.memory.component.MyButton
import uk.fernando.memory.component.MyDialog
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.safeNav
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.gold
import uk.fernando.memory.theme.grey
import uk.fernando.memory.theme.greyLight
import uk.fernando.memory.theme.red
import uk.fernando.memory.viewmodel.HomeViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeViewModel = getViewModel()
) {
    var currentLevel by remember { mutableStateOf<LevelEntity?>(null) }

    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            NavigationTopBar(
                onSettingsClick = { navController.safeNav(Directions.settings.path) }
            )

            //val pagerState = rememberPagerState(pageCount = 3)

            // Page Content
            HorizontalPager(
                //state = pagerState,
                count = viewModel.mapList.value.count(),
                modifier = Modifier
                    .weight(1f)
            ) { page ->
                MapContent(
                    list = viewModel.mapList.value[page].levelList,
                    onLevelClick = { level ->
                        if (level.starCount > 0)
                            currentLevel = level
                        else
                            navController.safeNav(Directions.game.withArgs("${level.id}", "${level.cardQuantity}"))
                    }
                )
            }

        }

        LevelDialog(level = currentLevel,
            onCancel = { currentLevel = null },
            onPlay = {
                navController.safeNav(Directions.game.withArgs("${currentLevel?.id}", "${currentLevel?.cardQuantity}"))
                currentLevel = null
            }
        )

    }

}

@Composable
private fun NavigationTopBar(onSettingsClick: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 6.dp),
            onClick = onSettingsClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun MapContent(list: List<LevelEntity>, onLevelClick: (LevelEntity) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(5)
    ) {
        items(list) { level ->
            LevelCard(level, onLevelClick)
        }
    }
}

@Composable
private fun LevelCard(level: LevelEntity, onClick: (LevelEntity) -> Unit) {

    Surface(
        modifier = Modifier.aspectRatio(1f),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.small,
        color = if (level.isDisabled) greyLight else MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small)
                .clickable { if (!level.isDisabled) onClick(level) },
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Center
            ) {

                if (level.isDisabled) {
                    Icon(
                        painterResource(id = R.drawable.ic_lock),
                        modifier = Modifier.fillMaxSize(0.8f),
                        contentDescription = null,
                        tint = Color.White.copy(0.8f)
                    )
                } else {
                    Text(
                        text = level.position.toString(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (level.starCount > 0) {
                    Box(
                        Modifier
                            .fillMaxHeight(0.3f)
                            .align(BottomCenter)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = gold
                        )

                        Icon(
                            Icons.Filled.Star,
                            modifier = Modifier.padding(start = 8.dp),
                            contentDescription = null,
                            tint = if (level.starCount > 1) gold else grey
                        )

                        Icon(
                            Icons.Filled.Star,
                            modifier = Modifier.padding(start = 16.dp),
                            contentDescription = null,
                            tint = if (level.starCount > 2) gold else grey
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun LevelDialog(level: LevelEntity?, onCancel: () -> Unit, onPlay: () -> Unit) {
    MyAnimation(level != null) {

        MyDialog {
            Column(Modifier.padding(16.dp)) {

                Text(
                    text = "Stars: ${level?.starCount} ",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Time: ${level?.time} ",
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(Modifier.padding(top = 32.dp)) {
                    MyButton(
                        modifier = Modifier.weight(1f),
                        onClick = onCancel,
                        color = red,
                        text = stringResource(R.string.close_action)
                    )

                    Spacer(Modifier.width(16.dp))

                    MyButton(
                        modifier = Modifier.weight(1f),
                        onClick = onPlay,
                        text = stringResource(R.string.play_action)
                    )
                }
            }
        }
    }
}
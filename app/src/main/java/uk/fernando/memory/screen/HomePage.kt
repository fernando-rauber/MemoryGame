package uk.fernando.memory.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import uk.fernando.memory.component.MyResultDialog
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.safeNav
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.dark
import uk.fernando.memory.theme.gold
import uk.fernando.memory.theme.green
import uk.fernando.memory.theme.greenLight
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

            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = stringResource(id = R.string.select_level_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            //val pagerState = rememberPagerState(pageCount = 3)

            // Page Content
            HorizontalPager(
                //state = pagerState,
                count = viewModel.mapList.value.count(),
                modifier = Modifier.weight(1f)
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
        columns = GridCells.Fixed(4)
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
        border = BorderStroke(4.dp, greenLight),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        .3f to MaterialTheme.colorScheme.primary,
                        1f to green.copy(0.7f)
                    )                )
                .clip(MaterialTheme.shapes.small)
                .clickable { if (!level.isDisabled) onClick(level) },
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (level.isDisabled) {
                    Icon(
                        painterResource(id = R.drawable.ic_lock),
                        modifier = Modifier.fillMaxSize(0.5f),
                        contentDescription = null,
                        tint = Color.White.copy(0.5f)
                    )
                } else {
                    Box(Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier
                                .align(Center)
                                .padding(top = 10.dp),
                            text = level.position.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth(.8f)
                            .padding(bottom = 5.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            modifier = Modifier.weight(1f),
                            contentDescription = null,
                            tint = if (level.starCount > 0) gold else dark
                        )

                        Icon(
                            Icons.Filled.Star,
                            modifier = Modifier.weight(1f),
                            contentDescription = null,
                            tint = if (level.starCount > 1) gold else dark
                        )

                        Icon(
                            Icons.Filled.Star,
                            modifier = Modifier.weight(1f),
                            contentDescription = null,
                            tint = if (level.starCount > 2) gold else dark
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

        level?.let {
            MyResultDialog(
                level = level,
                isFail = false,
                leftButtonText = R.string.close_action,
                rightButtonText = R.string.replay_action,
                onLeftButton = onCancel,
                onRightButton = onPlay,
            )
        }
    }
}
package uk.fernando.memory.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.R
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.safeNav
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.grey
import uk.fernando.memory.viewmodel.HomeViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeViewModel = getViewModel()
) {

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
                onLevelClick = { levelID -> navController.safeNav(Directions.game.withArgs("$levelID")) }
            )
        }

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
private fun MapContent(list: List<LevelEntity>, onLevelClick: (Int) -> Unit) {
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
private fun LevelCard(level: LevelEntity, onClick: (Int) -> Unit) {

    Surface(
        modifier = Modifier.aspectRatio(1f),
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.small,
        color = if (level.isDisabled) grey else MaterialTheme.colorScheme.surface
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.small)
                .clickable { if (!level.isDisabled) onClick(level.id!!) }) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = level.position.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
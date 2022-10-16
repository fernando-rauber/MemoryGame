package uk.fernando.memory.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.memory.R
import uk.fernando.memory.ads.MyAdBanner
import uk.fernando.memory.component.MyButtonIcon
import uk.fernando.memory.component.MyResultDialog
import uk.fernando.memory.component.MyStar
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.SCREEN_HEIGHT
import uk.fernando.memory.database.entity.CategoryWithLevel
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.getTypeName
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.gold
import uk.fernando.memory.theme.greenDark
import uk.fernando.memory.util.CardType
import uk.fernando.memory.viewmodel.HomeViewModel
import uk.fernando.util.component.MyAnimatedVisibility
import uk.fernando.util.component.MyIconButton
import uk.fernando.util.ext.clickableSingle
import uk.fernando.util.ext.safeNav

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeViewModel = getViewModel()
) {
    val prefs: PrefsStore by inject()
    val starsCount = prefs.getStarCount().collectAsState(initial = 0)
    val isPremium = prefs.isPremium().collectAsState(initial = false)
    var currentLevel by remember { mutableStateOf<LevelEntity?>(null) }
    val coroutine = rememberCoroutineScope()

    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            NavigationTopBar(
                starsCount = starsCount.value,
                onSettingsClick = { navController.safeNav(Directions.settings.path) }
            )

            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(id = R.string.select_level_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            val pagerState = rememberPagerState()

            // Page Content
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                count = viewModel.categoryList.value.count()
            ) { page ->

                PageContent(
                    item = viewModel.categoryList.value[page],
                    totalStars = starsCount.value,
                    onLevelClick = { level ->
                        if (level.star > 0)
                            currentLevel = level
                        else
                            navController.safeNav(Directions.game.withArgs("${level.id}", "${level.categoryID}"))
                    }
                )
            }

            Row(Modifier.padding(horizontal = 16.dp)) {
                MyAnimatedVisibility(pagerState.currentPage > 0) {
                    MyButtonIcon(
                        onClick = {
                            if (pagerState.currentPage > 0)
                                coroutine.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        },
                        icon = R.drawable.ic_arrow_back
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                MyAnimatedVisibility(pagerState.currentPage < viewModel.categoryList.value.count() - 1) {
                    MyButtonIcon(
                        onClick = {
                            if (pagerState.currentPage < viewModel.categoryList.value.count() - 1)
                                coroutine.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        },
                        icon = R.drawable.ic_arrow_forward
                    )
                }
            }

            if (!isPremium.value) {
                Spacer(Modifier.weight(1f))
                MyAdBanner(
                    placement = "Home_Screen",
                    modifier = Modifier
                        .defaultMinSize(minHeight = 50.dp)
                        .padding(top = 16.dp)
                )
            }
        }

        LevelDialog(
            level = currentLevel,
            onCancel = { currentLevel = null },
            onReplay = {
                navController.safeNav(Directions.game.withArgs("${currentLevel?.id}", "${currentLevel?.categoryID}"))
                currentLevel = null
            }
        )
    }
}

@Composable
private fun NavigationTopBar(starsCount: Int, onSettingsClick: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {

        Row(
            Modifier
                .align(CenterStart)
                .padding(start = 30.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(.15f), MaterialTheme.shapes.extraSmall.copy(topStart = CornerSize(0.dp))),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyStar(
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = (-16).dp)
            )

            Text(
                modifier = Modifier.offset(x = (-11).dp),
                text = "$starsCount/${MAX_CARDS_PER_CATEGORY * 3 * CardType.getQuantity()}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        MyIconButton(
            icon = R.drawable.ic_settings,
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onSettingsClick,
            tint = MaterialTheme.colorScheme.onBackground
        )

    }
}

@Composable
private fun PageContent(item: CategoryWithLevel, totalStars: Int, onLevelClick: (LevelEntity) -> Unit) {
    Column {
        if (totalStars < item.category.starsRequired) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.category.starsRequired}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    Icons.Filled.Star,
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(16.dp),
                    contentDescription = null,
                    tint = gold
                )
                Text(
                    text = stringResource(R.string.stars_required_unlock_args, stringResource(item.category.type.getTypeName())),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(item.category.type.getTypeName()),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        MapContent(
            list = item.levelList,
            onLevelClick = onLevelClick
        )
    }
}

@Composable
private fun MapContent(list: List<LevelEntity>, onLevelClick: (LevelEntity) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(if (SCREEN_HEIGHT < 700) 5 else 4)
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
        border = BorderStroke(2.dp, Color.White.copy(0.3f)),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        .3f to MaterialTheme.colorScheme.primary,
                        1f to greenDark.copy(0.7f)
                    )
                )
                .clip(MaterialTheme.shapes.small)
                .clickableSingle { if (!level.isDisabled) onClick(level) },
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
                            text = level.id.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth(.75f)
                            .padding(bottom = 7.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        MyStar(
                            modifier = Modifier.weight(.9f),
                            isBlack = level.star <= 0
                        )
                        MyStar(
                            modifier = Modifier.weight(1.1f),
                            isBlack = level.star <= 1
                        )
                        MyStar(
                            modifier = Modifier.weight(.9f),
                            isBlack = level.star <= 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelDialog(level: LevelEntity?, onCancel: () -> Unit, onReplay: () -> Unit) {
    MyAnimatedVisibility(level != null) {

        level?.let {
            MyResultDialog(
                level = level,
                leftButtonText = R.string.close_action,
                rightButtonText = R.string.replay_action,
                onClose = onCancel,
                onLeftButton = onCancel,
                onRightButton = onReplay,
            )
        }
    }
}
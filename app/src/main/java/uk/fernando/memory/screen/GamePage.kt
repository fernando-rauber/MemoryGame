package uk.fernando.memory.screen

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.advertising.component.AdBanner
import uk.fernando.memory.R
import uk.fernando.memory.activity.MainActivity
import uk.fernando.memory.component.MyAnimation
import uk.fernando.memory.component.MyFlipCard
import uk.fernando.memory.component.MyResultDialog
import uk.fernando.memory.config.AppConfig.COUNTDOWN_TIMER
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.getCellCount
import uk.fernando.memory.ext.getWidthSize
import uk.fernando.memory.ext.playAudio
import uk.fernando.memory.ext.safeNav
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.red
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.util.CardType
import uk.fernando.memory.viewmodel.GameViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    levelId: Int,
    viewModel: GameViewModel = getViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setUpGame(levelId).collect { isLevelDisabled ->
            if (isLevelDisabled) navController.popBackStack()
        }
    }

    val coroutine = rememberCoroutineScope()
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_interstitial_end_level))
    val soundCountDown = MediaPlayer.create(LocalContext.current, R.raw.sound_countdown)
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)

    Box {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            TopBar(
                viewModel = viewModel,
                levelId = levelId,
                onClose = { navController.popBackStack() }
            )

            CountDownAndAd(
                startSoundEffect = {
                    coroutine.launch(Dispatchers.IO) {
                        soundCountDown.playAudio(isSoundEnable.value)
                    }
                },
                onStart = { viewModel.startGame() }
            )

            CardList(viewModel)
        }

        // Dialogs
        DialogResult(
            viewModel = viewModel,
            fullScreenAd = fullScreenAd,
            onClose = { navController.popBackStack() },
            onReplayOrNextLevel = { id ->
                navController.popBackStack()
                navController.safeNav(Directions.game.withArgs("$id"))
            }
        )
    }
}

@Composable
private fun TopBar(viewModel: GameViewModel, levelId: Int, onClose: () -> Unit) {
    Box(
        Modifier
            .padding(start = 16.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {

        // Mistakes
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = stringResource(R.string.mistakes_args, viewModel.mistakes.value, MISTAKES_POSSIBLE),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Close button
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = onClose
        ) {
            Icon(
                painterResource(id = R.drawable.ic_close),
                contentDescription = "close",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Title
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.level_args, levelId.toString()),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun CountDownAndAd(startSoundEffect: () -> Unit, onStart: () -> Unit) {
    var countDown by remember { mutableStateOf(COUNTDOWN_TIMER) }

    LaunchedEffect(Unit) {
        if (countDown == 3)
            startSoundEffect()

        while (countDown > 0) {
            delay(1.seconds)
            countDown--
            if (countDown == 0)
                onStart()
        }
    }

    Box(
        Modifier
            .padding(top = 16.dp)
            .defaultMinSize(minHeight = 50.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = countDown > 0,
            exit = fadeOut()
        ) {
            Text(
                text = stringResource(R.string.turn_cards_back_args, "$countDown"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium
            )
        }

        if (countDown <= 0)
            AdBanner(unitId = stringResource(R.string.ad_banner_level))
    }
}

@Composable
private fun CardList(viewModel: GameViewModel) {
    Box(Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth(viewModel.quantity.value.getWidthSize())
                .align(Alignment.Center),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(viewModel.quantity.value.getCellCount())
        ) {
            items(viewModel.cardList) { card ->

                var state by remember { mutableStateOf(card.status) }

                state = card.status

                MyFlipCard(
                    cardFace = state,
                    onClick = { viewModel.setSelectedCard(card) },
                    back = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            ComponentByCardType(card)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ComponentByCardType(card: CardModel) {
    when (CardType.getByValue(card.type)) {
        CardType.ANIMAL -> {
//            Icon(
//                painterResource(id = card.id),
//                modifier = Modifier.fillMaxWidth(0.8f),
//                contentDescription = null,
//                tint = Color.Unspecified
//            )
            Text(
                text = "${card.id}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
        CardType.FLAG -> {
            Text(
                text = "${card.id}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Yellow
            )
        }
        else -> {
            Text(
                text = "${card.id}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun DialogResult(
    viewModel: GameViewModel,
    fullScreenAd: AdInterstitial,
    onClose: () -> Unit,
    onReplayOrNextLevel: (Int) -> Unit,
) {
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)
    val isPremium = prefs.isPremium().collectAsState(initial = false)
    val soundFinish = MediaPlayer.create(LocalContext.current, R.raw.sound_finish)

    MyAnimation(viewModel.levelResult.value != null) {
        LaunchedEffect(Unit) { soundFinish.playAudio(isSoundEnable.value) }

//        if (!isPremium.value)
//            fullScreenAd.showAdvert()

        viewModel.levelResult.value?.let { level ->
            MyResultDialog(
                level = level,
                leftButtonText = if (level.star > 0) R.string.replay_action else R.string.close_action,
                rightButtonText = if (level.star > 0) R.string.next_level else R.string.replay_action,
                onClose = onClose,
                onLeftButton = {
                    if (level.star > 0)
                        onReplayOrNextLevel(level.id!!) // replay
                    else
                        onClose()

                },
                onRightButton = {
                    if (level.star > 0)
                        onReplayOrNextLevel(level.id!! + 1)  // Next Level
                    else
                        onReplayOrNextLevel(level.id!!) // replay
                }
            )
        }
    }
}

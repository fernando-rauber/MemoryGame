package uk.fernando.memory.screen

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
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
import uk.fernando.memory.component.MyFlipCard
import uk.fernando.memory.component.MyResultDialog
import uk.fernando.memory.config.AppConfig.COUNTDOWN_TIMER
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.getBackgroundColor
import uk.fernando.memory.ext.getCellCount
import uk.fernando.memory.ext.getWidthSize
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.util.CardType
import uk.fernando.memory.viewmodel.GameViewModel
import uk.fernando.util.component.MyAnimatedVisibility
import uk.fernando.util.ext.playAudio
import uk.fernando.util.ext.safeNav
import kotlin.time.Duration.Companion.seconds

@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    levelId: Int,
    categoryId: Int,
    viewModel: GameViewModel = getViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.setUpGame(levelId, categoryId).collect { isLevelDisabled ->
            if (isLevelDisabled) navController.popBackStack()
        }
    }

    val coroutine = rememberCoroutineScope()
    val fullScreenAd = AdInterstitial(LocalContext.current as MainActivity, stringResource(R.string.ad_interstitial_end_level))
    val soundCountDown = MediaPlayer.create(LocalContext.current, R.raw.sound_countdown)
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)
    val isPremium = prefs.isPremium().collectAsState(initial = false)

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
                isPremium = isPremium.value,
                startSoundEffect = {
                    coroutine.launch(Dispatchers.IO) { soundCountDown.playAudio(isSoundEnable.value) }
                },
                onStart = { viewModel.startGame() }
            )

            CardList(viewModel, isSoundEnable.value)
        }

        // Dialogs
        DialogResult(
            viewModel = viewModel,
            isPremium = isPremium.value,
            isSoundEnable = isSoundEnable.value,
            fullScreenAd = fullScreenAd,
            onClose = { navController.popBackStack() },
            onReplayOrNextLevel = { id, category ->
                val levelID = if (id > MAX_CARDS_PER_CATEGORY) 1 else id
                val categoryID = if (id > MAX_CARDS_PER_CATEGORY) category + 1 else category

                navController.popBackStack()
                navController.safeNav(Directions.game.withArgs("$levelID", "$categoryID"))
            }
        )
    }
}

@Composable
private fun TopBar(viewModel: GameViewModel, levelId: Int, onClose: () -> Unit) {
    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {

        // Mistakes
        Row(
            Modifier
                .align(Alignment.CenterStart)
                .padding(start = 30.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(.15f), MaterialTheme.shapes.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = (-16).dp),
                painter = painterResource(R.drawable.img_mistakes),
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .offset(x = (-11).dp),
                text = "${viewModel.mistakes.value}/$MISTAKES_POSSIBLE",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

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
            text = stringResource(id = R.string.level_args, "$levelId"),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun CountDownAndAd(isPremium: Boolean, startSoundEffect: () -> Unit, onStart: () -> Unit) {
    var countDown by remember { mutableStateOf(COUNTDOWN_TIMER) }

    LaunchedEffect(Unit) {

        while (countDown > 0) {
            if (countDown == 3)
                startSoundEffect()

            delay(1.seconds)
            countDown--
            if (countDown == 0)
                onStart()
        }
    }

    Box(
        Modifier
            .padding(top = 8.dp)
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
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }

        if (countDown == 0 && !isPremium)
            AdBanner(R.string.ad_banner_level)
    }
}

@Composable
private fun CardList(viewModel: GameViewModel, isSoundEnable: Boolean) {
    val coroutine = rememberCoroutineScope()
    val soundCorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_correct)
    val soundIncorrect = MediaPlayer.create(LocalContext.current, R.raw.sound_incorrect)

    Box(Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth(viewModel.quantity.value.getWidthSize())
                .align(Alignment.Center),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(viewModel.quantity.value.getCellCount())
        ) {
            items(viewModel.cardList) { card ->

                var state by remember { mutableStateOf(card.status) }

                state = card.status

                MyFlipCard(
                    cardFace = state,
                    onClick = {
                        coroutine.launch {
                            viewModel.setSelectedCard(card).collect { isCorrect ->
                                isCorrect?.let {
                                    if (isCorrect)
                                        soundCorrect.playAudio(isSoundEnable)
                                    else
                                        soundIncorrect.playAudio(isSoundEnable)
                                }
                            }
                        }
                    },
                    back = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(card.id.getBackgroundColor()),
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
        CardType.ANIMAL, CardType.FOOD, CardType.TREE -> {
            Image(
                painterResource(id = card.id),
                modifier = Modifier.fillMaxWidth(0.7f),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
        CardType.FLAG, CardType.TILE -> {
            Image(
                painterResource(id = card.id),
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }
        else -> {
            Text(
                text = "${card.id}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DialogResult(
    viewModel: GameViewModel,
    isPremium: Boolean,
    isSoundEnable: Boolean,
    fullScreenAd: AdInterstitial,
    onClose: () -> Unit,
    onReplayOrNextLevel: (Int, Int) -> Unit,
) {
    val context = LocalContext.current

    MyAnimatedVisibility(viewModel.levelResult.value != null) {
        viewModel.levelResult.value?.let { level ->

            LaunchedEffect(Unit) { MediaPlayer.create(context, if (level.star > 0) R.raw.sound_finish else R.raw.sound_game_over).playAudio(isSoundEnable) }

            if (!isPremium)
                fullScreenAd.showAdvert()

            MyResultDialog(
                level = level,
                leftButtonText = if (level.star > 0) R.string.replay_action else R.string.close_action,
                rightButtonText = if (level.star > 0) R.string.next_level else R.string.replay_action,
                onClose = onClose,
                onLeftButton = {
                    if (level.star > 0)
                        onReplayOrNextLevel(level.id, level.categoryID) // replay
                    else
                        onClose()

                },
                onRightButton = {
                    if (level.star > 0)
                        onReplayOrNextLevel(level.id + 1, level.categoryID)  // Next Level
                    else
                        onReplayOrNextLevel(level.id, level.categoryID) // replay
                }
            )
        }
    }
}

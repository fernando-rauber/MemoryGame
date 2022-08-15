package uk.fernando.memory.screen

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.advertising.AdInterstitial
import uk.fernando.memory.R
import uk.fernando.memory.activity.MainActivity
import uk.fernando.memory.component.MyAnimation
import uk.fernando.memory.component.MyButton
import uk.fernando.memory.component.MyDialog
import uk.fernando.memory.component.MyFlipCard
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.*
import uk.fernando.memory.theme.green
import uk.fernando.memory.viewmodel.GameViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun GamePage(
    navController: NavController = NavController(LocalContext.current),
    levelId: Int,
    cardQtd: Int,
    viewModel: GameViewModel = getViewModel()
) {
    LaunchedEffect(Unit) { viewModel.setUpGame(levelId, cardQtd) }

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

            CardList(viewModel, cardQtd)
        }

        // Dialogs
        CountDown(
            startSoundEffect = {
                coroutine.launch(Dispatchers.IO) {
                    soundCountDown.playAudio(isSoundEnable.value)
                }
            },
            onStart = { viewModel.startGame() }
        )

        DialogResult(
            viewModel = viewModel,
            fullScreenAd = fullScreenAd,
            onExit = { navController.popBackStack() }
        )
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
private fun CardList(viewModel: GameViewModel, cardQtd: Int) {
    Box(Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(cardQtd.getWidthSize())
                .align(Alignment.Center),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(cardQtd.getCellCount())
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
}

@Composable
fun CountDown(startSoundEffect: () -> Unit, onStart: () -> Unit) {
    var countDown by remember { mutableStateOf(5) }

    LaunchedEffect(Unit) {
        if (countDown == 3)
            startSoundEffect()

        while (countDown >= 0) {
            delay(1.seconds)
            countDown--
            if (countDown == -1)
                onStart()
        }
    }

    AnimatedVisibility(
        visible = countDown > 0,
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .noRippleClickable { }
                .background(Color.Black.copy(0.6f))
        ) {

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "$countDown",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 200.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DialogResult(
    viewModel: GameViewModel,
    fullScreenAd: AdInterstitial,
    onExit: () -> Unit
) {
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)
    val isPremium = prefs.isPremium().collectAsState(initial = false)
    val soundFinish = MediaPlayer.create(LocalContext.current, R.raw.sound_finish)

    MyAnimation(viewModel.isGameFinished.value) {
        LaunchedEffect(Unit) { soundFinish.playAudio(isSoundEnable.value) }

//        if (!isPremium.value)
//            fullScreenAd.showAdvert()

        MyDialog {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

//                Icon(
//                    modifier = Modifier.padding(vertical = 30.dp).fillMaxWidth(0.6f),
//                    painter = painterResource(id = image),
//                    contentDescription = null,
//                    tint = Color.Unspecified
//                )

                Text(
                    modifier = Modifier.padding(vertical = 15.dp),
                    text = "End Game",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    letterSpacing = 0.30.sp
                )

                MyButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp),
                    onClick = onExit,
                    text = "exit"
                )

                MyButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 50.dp),
                    onClick = {},
                    text = "Retry or next level"
                )
            }
        }
    }
}

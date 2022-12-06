package uk.fernando.memory.screen.custom

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.memory.R
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.screen.campaign.CardList
import uk.fernando.memory.screen.campaign.CountDownAndAd
import uk.fernando.memory.viewmodel.custom.CustomGameViewModel
import uk.fernando.util.component.MyAnimatedVisibility
import uk.fernando.util.component.MyButton
import uk.fernando.util.component.MyDialog
import uk.fernando.util.ext.playAudio
import java.util.*

@Composable
fun CustomGamePage(
    navController: NavController = NavController(LocalContext.current),
    boardSize: Int,
    viewModel: CustomGameViewModel = getViewModel()
) {
    LaunchedEffect(Unit) { viewModel.setUpGame() }

    val coroutine = rememberCoroutineScope()
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
                onClose = { navController.popBackStack() }
            )

            CountDownAndAd(
                isPremium = isPremium.value,
                startSoundEffect = {
                    coroutine.launch(Dispatchers.IO) { soundCountDown.playAudio(isSoundEnable.value) }
                },
                onStart = { viewModel.startGame() }
            )

            CardList(viewModel, isSoundEnable.value, boardSize)
        }

        // Dialogs
        MyAnimatedVisibility(viewModel.result.value != null) {
            viewModel.result.value?.let { history ->
                DialogResult(
                    score = history,
                    isSoundEnable = isSoundEnable.value,
                    isPremium = isPremium.value,
//            fullScreenAd = fullScreenAd,
                    onClose = { navController.popBackStack() }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(viewModel: CustomGameViewModel, onClose: () -> Unit) {
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
                text = "${viewModel.mistakes.value}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        // Close button
        Card(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            onClick = onClose,
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(Color.White),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                modifier = Modifier.padding(8.dp),
                contentDescription = null,
                tint = Color.Black.copy(.7f)
            )
        }
    }
}

@Composable
fun DialogResult(
    score: ScoreEntity,
    isSoundEnable: Boolean,
    isPremium : Boolean,
//    fullScreenAd: AdInterstitial,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { MediaPlayer.create(context, R.raw.sound_finish).playAudio(isSoundEnable) }

//    if (!isPremium)
    //fullScreenAd.showAdvert()

    MyDialog {
        Column(
            Modifier.padding( 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp)
                    .offset(y = -(30).dp),
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(50),
                border = BorderStroke(2.dp, Color.White.copy(0.2f))
            ) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.completed).uppercase(Locale.ENGLISH),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Mistakes
            Text(
                modifier = Modifier.padding(vertical = 30.dp),
                text = stringResource(id = R.string.mistakes_dialog_args, score.attempts),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            MyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                onClick = onClose,
                color = MaterialTheme.colorScheme.surface,
                text = stringResource(R.string.close_action)
            )

        }
    }
}
package uk.fernando.memory.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.component.MyButton
import uk.fernando.memory.component.MyFlipCard
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.navigation.Directions
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

        Text(
            text = "LEVEL \n$levelId ",
            style = MaterialTheme.typography.bodyLarge
        )


        MyButton(onClick = { navController.popBackStack() }, text = "Just popup")

        Spacer(modifier = Modifier.height(50.dp))

        MyButton(onClick = {
            viewModel.updateLevel(1, 25, levelId)
            navController.popBackStack()
        }, text = "Update Level and popup")

        TopBar()

        CardList(viewModel)
    }
}

@Composable
private fun TopBar() {
    // Time

    // Level Text

    // Pause or Close?
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

            Log.e(TAG, "CardList updated $card ", )
//            var state by remember { mutableStateOf(CardFace.Front) }

//            viewModel.flipBackCard.value.let { update ->
//                if (state == CardFace.Back) {
//                    state = CardFace.Front
//                }
//            }
//                if (update % 2 == 0 && state == CardFace.Back) {
//                    state = CardFace.Front
//                }


            MyFlipCard(
                cardFace = state,
                onClick = {
                    viewModel.setSelectedCard(card)
                },
                back = {
                    Text(
                        text = "Front ${card.id}", Modifier
                            .background(Color.Red)
                    )
                }
            )
        }
    }
}

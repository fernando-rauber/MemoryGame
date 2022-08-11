package uk.fernando.memory.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.component.MyButton
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


        MyButton(onClick = { navController.popBackStack() }, text = "Just popup")

        Spacer(modifier = Modifier.height(50.dp))

        MyButton(onClick = {
            viewModel.updateLevel(1, 25, levelId)
            navController.popBackStack()
        }, text = "Update Level and popup")

    }
}
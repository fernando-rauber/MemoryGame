package uk.fernando.memory.screen.custom

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.component.NavigationBarTop
import uk.fernando.memory.viewmodel.custom.CreateGameViewModel
import uk.fernando.memory.R
import uk.fernando.memory.component.MyDivider
import uk.fernando.memory.ext.getCategoryIcon
import uk.fernando.memory.ext.getCategoryName
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.theme.dark
import uk.fernando.memory.theme.greyLight
import uk.fernando.memory.theme.orange
import uk.fernando.util.component.MyButton
import uk.fernando.util.ext.safeNav

@Composable
fun CreateGamePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: CreateGameViewModel = getViewModel()
) {

    Column(Modifier.fillMaxSize()) {

        NavigationBarTop(
            title = R.string.game_creation_title,
            onBackClick = { navController.popBackStack() }
        )

        Surface(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            shadowElevation = 7.dp,
            shape = MaterialTheme.shapes.medium
        ) {

            Box(Modifier.padding(16.dp)) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    MyBoardSize(viewModel)

                    MyDivider()

                    MyCategories(viewModel)

                    Spacer(Modifier.height(66.dp))
                }

                MyButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .defaultMinSize(minHeight = 50.dp),
                    text = stringResource(id = R.string.start_action),
                    enabled = viewModel.isGameValid.value,
                    color = orange,
                    shape = ButtonDefaults.shape,
                    onClick = { navController.safeNav(Directions.customGame.withArgs("${viewModel.boardSize.value}")) }
                )

            }
        }
    }
}

@Composable
private fun ColumnScope.MyBoardSize(viewModel: CreateGameViewModel) {
    Text(
        text = stringResource(R.string.board_size),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )

    Slider(
        value = viewModel.boardSize.value.toFloat(),
        //colors = SliderDefaults.colors(dark, dark, Color.White, greyLight, dark),
        onValueChange = { viewModel.setBoardSize(it.toInt()) },
        steps = 6,
        valueRange = 3f..10f,
    )

    Text(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        text = "${viewModel.boardSize.value}X${viewModel.boardSize.value}",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun MyCategories(viewModel: CreateGameViewModel) {

    val categories = viewModel.categories

    Column {
        Text(
            text = stringResource(id = R.string.categories),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyChip((1).getCategoryName(), (1).getCategoryIcon(), isSelected = categories.contains(1)) { viewModel.setCategories(1) }
            MyChip((2).getCategoryName(), (2).getCategoryIcon(), isSelected = categories.contains(2)) { viewModel.setCategories(2) }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyChip((3).getCategoryName(), (3).getCategoryIcon(), isSelected = categories.contains(3)) { viewModel.setCategories(3) }
            MyChip((4).getCategoryName(), (4).getCategoryIcon(), isSelected = categories.contains(4)) { viewModel.setCategories(4) }
        }

        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MyChip((5).getCategoryName(), (5).getCategoryIcon(), isSelected = categories.contains(5)) { viewModel.setCategories(5) }
            MyChip((6).getCategoryName(), (6).getCategoryIcon(), isSelected = categories.contains(6)) { viewModel.setCategories(6) }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyChip(@StringRes text: Int, @DrawableRes icon: Int, isSelected: Boolean, color: Color = dark, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(if (isSelected) color else greyLight),
    ) {
        Row(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 24.dp, vertical = 3.dp)
        ) {

            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = stringResource(id = text),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
        }
    }
}
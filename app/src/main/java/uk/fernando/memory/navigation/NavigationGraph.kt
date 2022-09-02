package uk.fernando.memory.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import uk.fernando.memory.navigation.Directions.LEVEL_ID
import uk.fernando.memory.screen.GamePage
import uk.fernando.memory.screen.HomePage
import uk.fernando.memory.screen.SettingsPage
import uk.fernando.memory.screen.SplashPage


@ExperimentalAnimationApi
fun NavGraphBuilder.buildGraph(navController: NavController) {
    composable(Directions.splash.path) {
        SplashPage(navController)
    }

    composable(Directions.home.path) {
        HomePage(navController)
    }

    composable(Directions.game.withArgsFormat(LEVEL_ID)) {
        val levelId = it.arguments?.getString(LEVEL_ID)

        if (levelId == null)
            navController.popBackStack()
        else
            GamePage(navController, levelId.toInt())
    }

    composable(Directions.settings.path) {
        SettingsPage(navController)
    }
}


package uk.fernando.memory.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import uk.fernando.memory.navigation.Directions.CATEGORY_ID
import uk.fernando.memory.navigation.Directions.LEVEL_ID
import uk.fernando.memory.screen.SettingsPage
import uk.fernando.memory.screen.SplashPage
import uk.fernando.memory.screen.campaign.GamePage
import uk.fernando.memory.screen.campaign.LevelPage


@ExperimentalAnimationApi
fun NavGraphBuilder.buildGraph(navController: NavController) {
    composable(Directions.splash.path) {
        SplashPage(navController)
    }

    composable(Directions.level.path) {
        LevelPage(navController)
    }

    composable(Directions.campaignGame.withArgsFormat(LEVEL_ID, CATEGORY_ID)) {
        val levelId = it.arguments?.getString(LEVEL_ID)
        val categoryId = it.arguments?.getString(CATEGORY_ID)

        if (levelId == null || categoryId == null)
            navController.popBackStack()
        else
            GamePage(navController, levelId.toInt(), categoryId.toInt())
    }

    composable(Directions.settings.path) {
        SettingsPage(navController)
    }
}


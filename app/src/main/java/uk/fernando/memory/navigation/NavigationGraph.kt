package uk.fernando.memory.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
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
    composable(Directions.splash.path,
        exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700)) }) {
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

    composable(Directions.settings.path,
        enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700)) }
    ) {
        SettingsPage(navController)
    }

}


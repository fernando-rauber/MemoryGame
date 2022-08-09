package uk.fernando.memory.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import uk.fernando.memory.screen.HomePage
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

//    composable(Directions.countDown.withArgsFormat(SECONDS, REST_TYPE)) {
//        val seconds = it.arguments?.getString(SECONDS)
//        val isExerciseRest = it.arguments?.getString(REST_TYPE) == "true"
//        if (seconds == null)
//            navController.popBackStack()
//        else
//            CountDownPage(navController, seconds.toInt(),isExerciseRest)
//    }

//    composable(Directions.settings.path,
//        enterTransition = { slideIntoContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(700)) },
//        exitTransition = { slideOutOfContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(700)) }
//    ) {
//        SettingsPage(navController)
//    }

}


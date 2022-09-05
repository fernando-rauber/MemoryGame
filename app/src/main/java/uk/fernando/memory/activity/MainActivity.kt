package uk.fernando.memory.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import uk.fernando.memory.component.UpdateStatusBar
import uk.fernando.memory.config.AppConfig
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.navigation.buildGraph
import uk.fernando.memory.theme.MemoryTheme
import uk.fernando.memory.theme.dark

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberAnimatedNavController()

            UpdateStatusBar(color = dark)

            // set the device size
            AppConfig.SCREEN_HEIGHT = LocalConfiguration.current.screenHeightDp

            MemoryTheme {

                Box(Modifier.background(dark)) {
                    AnimatedNavHost(
                        navController = controller,
                        startDestination = Directions.splash.path
                    ) {
                        buildGraph(controller)
                    }
                }
            }
        }
    }
}
package uk.fernando.memory.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.inject
import uk.fernando.memory.component.UpdateStatusBar
import uk.fernando.memory.config.AppConfig
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.navigation.buildGraph
import uk.fernando.memory.theme.MemoryTheme
import uk.fernando.memory.theme.dark
import uk.fernando.memory.theme.whiteBackGround

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberAnimatedNavController()

            val prefs: PrefsStore by inject()
            val isDarkMode = prefs.isDarkMode().collectAsState(initial = false)

            val backgroundColor = if (isDarkMode.value) dark else whiteBackGround

            UpdateStatusBar(color = backgroundColor)

            // set the device size
            AppConfig.SCREEN_HEIGHT = LocalConfiguration.current.screenHeightDp

            MemoryTheme(isDarkMode.value) {

                Box(Modifier.background(backgroundColor)) {
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
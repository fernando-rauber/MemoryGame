package uk.fernando.memory.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.inject
import uk.fernando.memory.component.UpdateStatusBar
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.navigation.buildGraph
import uk.fernando.memory.R
import uk.fernando.memory.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberAnimatedNavController()

            val prefs: PrefsStore by inject()
            val isDarkMode = prefs.isDarkMode().collectAsState(initial = false)

            val backgroundColor = if (isDarkMode.value) dark else whiteBackGround

            UpdateStatusBar(color = backgroundColor)

            MemoryTheme(isDarkMode.value) {

                Box(Modifier.background(backgroundColor)) {
//                    Icon(
//                        painter = painterResource(R.drawable.img_moon),
//                        contentDescription = null,
//                        tint = orange.copy(0.15f)
//                    )

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
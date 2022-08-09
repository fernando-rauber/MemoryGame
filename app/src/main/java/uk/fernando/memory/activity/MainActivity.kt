package uk.fernando.memory.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.inject
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.navigation.Directions
import uk.fernando.memory.navigation.buildGraph
import uk.fernando.memory.theme.MemoryTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val controller = rememberAnimatedNavController()
//            val navBackStackEntry by controller.currentBackStackEntryAsState()

            val prefs: PrefsStore by inject()
            val isDarkMode = prefs.isDarkMode().collectAsState(initial = false)


            MemoryTheme(isDarkMode.value) {

                Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->

                    Column(modifier = Modifier.padding(padding)) {
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
}
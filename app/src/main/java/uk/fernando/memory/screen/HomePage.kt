package uk.fernando.memory.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import org.koin.androidx.compose.getViewModel
import uk.fernando.memory.viewmodel.HomeViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeViewModel = getViewModel()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        //val pagerState = rememberPagerState(pageCount = 3)

        // Page Content
        HorizontalPager(
            //state = pagerState,
            count = viewModel.mapList.value.count(),
            modifier = Modifier
                .weight(1f)
        ) { page ->

            Text(text = "hiiiii ${viewModel.mapList.value[page].map.id}")
            // Pages
        }

    }

}

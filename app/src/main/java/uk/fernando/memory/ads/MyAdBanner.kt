package uk.fernando.memory.ads

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import uk.fernando.memory.activity.MainActivity

@Composable
fun MyAdBanner(placement: String, modifier: Modifier = Modifier) {
    val adView = rememberAdMobWithLifecycle(placement)

    AndroidView(
        factory = { adView },
        modifier = modifier.defaultMinSize(minHeight = 1.dp)
    )
}

@Composable
private fun rememberAdMobWithLifecycle(placement: String): IronSourceBannerLayout {
    val context = LocalContext.current as MainActivity

    val banner = remember { IronSource.createBanner(context, ISBannerSize.BANNER) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(key1 = lifecycle, key2 = banner) {
        val lifecycleObserver = getAdLifecycleObserver(banner, placement)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return banner
}

private fun getAdLifecycleObserver(banner: IronSourceBannerLayout, placement: String): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> if (!banner.isDestroyed) IronSource.loadBanner(banner, placement)
            Lifecycle.Event.ON_PAUSE -> if (!banner.isDestroyed) IronSource.destroyBanner(banner)
            else -> {}
        }
    }

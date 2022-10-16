package uk.fernando.memory.ads

import android.util.Log
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InterstitialListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AdInterstitial(private val placement: String) {

    private val adState = MutableStateFlow(AdState.LOADING)

    init {
        initInterstitialAd()
    }

    private fun initInterstitialAd() {
        IronSource.setInterstitialListener(object : InterstitialListener {
            @Override
            override fun onInterstitialAdReady() {
                Log.d(TAG, "Ad is ready.")
                adState.value = AdState.READY
            }

            @Override
            override fun onInterstitialAdLoadFailed(error: IronSourceError) {
                Log.d(TAG, error.errorMessage)
                adState.value = AdState.FAIL
            }

            @Override
            override fun onInterstitialAdOpened() {
                adState.value = AdState.OPENED
                Log.d(TAG, "Ad showed fullscreen content.")
            }

            @Override
            override fun onInterstitialAdClosed() {
                adState.value = AdState.DISMISSED
                Log.d(TAG, "Ad was dismissed.")
            }

            @Override
            override fun onInterstitialAdShowFailed(error: IronSourceError) {
            }

            @Override
            override fun onInterstitialAdClicked() {
            }

            @Override
            override fun onInterstitialAdShowSucceeded() {
            }
        })

        IronSource.loadInterstitial()
    }

//    private fun initInterstitialAd() {
//        InterstitialAd.load(activity, unitID, AdRequest.Builder().build(),
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    Log.d(TAG, adError.message)
//                    adState.value = AdState.FAIL
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    Log.d(TAG, "Ad was loaded.")
//                    adState.value = AdState.LOADED
//                    mInterstitialAd = interstitialAd
//
//                    setFullScreenContentCallback()
//                }
//            })
//    }

//    private fun setFullScreenContentCallback() {
//        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                adState.value = AdState.DISMISSED
//                Log.d(TAG, "Ad was dismissed.")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                Log.d(TAG, "Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                adState.value = AdState.OPENED
//                Log.d(TAG, "Ad showed fullscreen content.")
//                mInterstitialAd = null
//            }
//        }
//    }

    fun showAdvert(): Flow<AdState> {
        if (IronSource.isInterstitialReady())
            IronSource.showInterstitial(placement)

        return adState
    }

    companion object {
        private const val TAG = "AdInterstitial"
    }
}
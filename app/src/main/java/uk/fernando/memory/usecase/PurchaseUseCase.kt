package uk.fernando.memory.usecase

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.fernando.billing.BillingHelper
import uk.fernando.billing.BillingState
import uk.fernando.memory.R
import uk.fernando.logger.MyLogger
import uk.fernando.memory.BuildConfig
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.ext.isNetworkAvailable
import uk.fernando.memory.util.Resource

const val PREMIUM_PRODUCT = "memory_premium"

class PurchaseUseCase(
    private val application: Application,
    private val prefs: PrefsStore,
    private val logger: MyLogger
) {

    private var billingHelper: BillingHelper? = null
    private val _billingState = MutableStateFlow<Resource<Int>?>(null)
    private var isPremium = false

    val billingState
        get() = _billingState.asStateFlow()

    fun startInAppPurchaseJourney(scope: CoroutineScope) {
        _billingState.value = Resource.Loading(false)

        if (application.isNetworkAvailable()) {

            scope.launch {
                val isPremiumDS = prefs.isPremium().first()
                isPremium = isPremiumDS

                if (!isPremium) {
                    billingHelper = BillingHelper.getInstance(
                        application,
                        scope,
                        arrayOf(PREMIUM_PRODUCT), // one only purchase
                        arrayOf(), // subscription
                        BuildConfig.BILLING_PUBLIC_KEY
                    )

                    observeBillingState()
                }
            }
        }
    }

    fun requestPayment(activity: Activity, scope: CoroutineScope) {
        if (!application.isNetworkAvailable()) {
            _billingState.value = Resource.Error("", R.string.internet_required)
            return
        }

        scope.launch {
            if (billingHelper == null) {
                startInAppPurchaseJourney(scope)
                delay(1000)
            }
            billingHelper?.launchBillingFlow(activity, PREMIUM_PRODUCT)
        }
    }

    fun restorePremium(scope: CoroutineScope) {
        if (!application.isNetworkAvailable()) {
            _billingState.value = Resource.Error("", R.string.internet_required)
            return
        }

        if (isPremium)
            _billingState.value = Resource.Success(R.string.restore_restored)
        else
            scope.launch {
                if (billingHelper == null) {
                    startInAppPurchaseJourney(scope)
                    delay(1000)
                }

                val isPurchased = billingHelper?.isPurchased(PREMIUM_PRODUCT)?.distinctUntilChanged()?.first()

                if (isPurchased == true) {
                    prefs.storePremium(true)
                    _billingState.value = Resource.Success(R.string.restore_restored)
                } else {
                    _billingState.value = Resource.Error("", R.string.restore_not_found)
                }
            }
    }

    private suspend fun observeBillingState() {
        billingHelper?.getBillingState()?.collect { state ->
            when (state) {
                is BillingState.Error -> {
                    _billingState.value = Resource.Error("", R.string.purchase_error)
                    logger.e(TAG, state.message)
                    logger.addMessageToCrashlytics(TAG, "Error - Purchase In App: msg: ${state.message}")
                }
                is BillingState.Success -> {
                    _billingState.value = Resource.Success(R.string.purchase_success)
                    prefs.storePremium(true)
                }
                is BillingState.Crashlytics -> {
                    logger.e(TAG, state.message)
                    logger.addMessageToCrashlytics(TAG, "CrashAnalytics - Purchase In App: msg: ${state.message}")
                }
                else -> {}
            }
        }
    }
}
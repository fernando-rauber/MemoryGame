package uk.fernando.memory.viewmodel

import android.app.Activity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.usecase.PurchaseUseCase
import uk.fernando.memory.util.Resource
import uk.fernando.snackbar.SnackBarSealed

class SettingsViewModel(
    private val useCase: PurchaseUseCase,
    private val prefs: PrefsStore
) : BaseViewModel() {

    private val _snackBar = MutableStateFlow<SnackBarSealed?>(null)
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    val snackBar: StateFlow<SnackBarSealed?>
        get() = _snackBar.asStateFlow()

    init {
        initialiseBillingHelper()
    }

    fun updateSound(enable: Boolean) {
        launchIO { prefs.storeSound(enable) }
    }

    private fun initialiseBillingHelper() {
        useCase.startInAppPurchaseJourney(scope)

        scope.launch {
            useCase.billingState.collect() { state ->

                when (state) {
                    is Resource.Error -> _snackBar.value = SnackBarSealed.Error(state.data)
                    is Resource.Success -> _snackBar.value = SnackBarSealed.Success(state.data)
                    else -> {}
                }
            }
        }
    }

    fun requestPayment(activity: Activity) {
        useCase.requestPayment(activity, scope)
    }

    fun restorePremium() {
        useCase.restorePremium(scope)
    }

    override fun onCleared() {
        scope.cancel()
    }
}
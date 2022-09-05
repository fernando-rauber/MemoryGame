package uk.fernando.memory.viewmodel

import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.usecase.SetUpUseCase

class SplashViewModel(private val prefs: PrefsStore, private val setUpUseCase: SetUpUseCase) : BaseViewModel() {

    fun firstSetUp() {
        launchIO {
            if (prefs.getVersion() == 1) {
                prefs.storeVersion(2)

                // Create all Levels
                setUpUseCase.invoke()
            }
        }
    }
}
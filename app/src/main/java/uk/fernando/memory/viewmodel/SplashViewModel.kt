package uk.fernando.memory.viewmodel

import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.usecase.SetUpUseCase

class SplashViewModel(private val prefs: PrefsStore, private val setUpUseCase: SetUpUseCase) : BaseViewModel() {

    fun setUp() {
        launchIO {
            if (prefs.getVersion() == 1) {
                prefs.storeVersion(3)

                // Create all Levels
                setUpUseCase.invoke()
            }
            if (prefs.getVersion() == 2) {
                prefs.storeVersion(3)

                setUpUseCase.updateVersion2()
            }
        }
    }
}
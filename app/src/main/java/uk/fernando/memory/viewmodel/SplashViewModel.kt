package uk.fernando.memory.viewmodel

import uk.fernando.memory.datastore.PrefsStore

class SplashViewModel(private val prefs: PrefsStore) : BaseViewModel() {

    fun firstSetUp(isDarkMode: Boolean) {
        launchIO {
            if (prefs.getVersion() == 1) {
                prefs.storeDarkMode(isDarkMode)
                prefs.storeVersion(2)

            }
        }
    }
}
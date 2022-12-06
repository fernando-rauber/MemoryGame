package uk.fernando.memory.viewmodel.custom

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import uk.fernando.memory.datastore.GamePrefsStore
import uk.fernando.memory.viewmodel.BaseViewModel

class CreateGameViewModel(private val prefsStore: GamePrefsStore) : BaseViewModel() {

    val boardSize = mutableStateOf(3)
    val isGameValid = mutableStateOf(false)

    private val _categories = mutableStateListOf<Int>()
    val categories: List<Int> = _categories

    init {
        launchDefault {
            boardSize.value = prefsStore.getBoardSize()
            _categories.addAll(prefsStore.getCategoryList())

            checkIfGameValid()
        }
    }

    fun setBoardSize(size: Int) {
        launchDefault {
            boardSize.value = size
            prefsStore.storeBoardSize(size)
        }
    }

    fun setCategories(category: Int) {
        launchDefault {
            if(categories.contains(category))
                _categories.remove(category)
            else
                _categories.add(category)

            prefsStore.storeCategory(categories)

            checkIfGameValid()
        }
    }

    private fun checkIfGameValid(){
        isGameValid.value = categories.size > 1
    }
}
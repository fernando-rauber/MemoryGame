package uk.fernando.memory.viewmodel

import uk.fernando.memory.usecase.UpdateLevelUseCase

class GameViewModel(private val updateLevelUseCase: UpdateLevelUseCase) : BaseViewModel() {

//    val levelList = mutableStateOf(emptyList<MapWithLevel>()) // Need to generate 24 or 30 card


    fun updateLevel(stars: Int, time: Int, levelID: Int) {
        launchDefault {
            updateLevelUseCase.invoke(stars, time, levelID)
        }
    }

}
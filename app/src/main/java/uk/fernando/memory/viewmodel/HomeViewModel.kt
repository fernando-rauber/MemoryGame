package uk.fernando.memory.viewmodel

import androidx.compose.runtime.mutableStateOf
import uk.fernando.memory.database.entity.MapEntity
import uk.fernando.memory.database.entity.MapWithLevel
import uk.fernando.memory.usecase.GetMapListUseCase

class HomeViewModel(
    private val getMapListUseCase: GetMapListUseCase,
) : BaseViewModel() {

    val mapList = mutableStateOf(emptyList<MapWithLevel>())

    init {
        launchDefault {
            getMapListUseCase.invoke().collect() { list ->
                mapList.value = list
            }
        }
    }

}
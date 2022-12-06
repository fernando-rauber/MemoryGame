package uk.fernando.memory.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.Flow
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.util.CardModel

interface GameViewData {
    fun updateMistakes(value: Int)
    fun endGame(level: LevelEntity?, score: ScoreEntity?)
    fun startGame(cards: List<CardModel>)
    fun updateCardStatus(card: CardModel, status: CardFace): CardModel
}

abstract class BaseGameViewModel : BaseViewModel() {

    protected val _cardList = mutableStateListOf<CardModel>()
    val cardList: List<CardModel> = _cardList
    val mistakes = mutableStateOf(0)
    val quantity = mutableStateOf(0)

    abstract fun startGame()
    abstract fun setSelectedCard(card: CardModel) : Flow<Boolean?>

}

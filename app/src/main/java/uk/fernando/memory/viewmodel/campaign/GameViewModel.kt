package uk.fernando.memory.viewmodel.campaign

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.usecase.GameUseCase
import uk.fernando.memory.usecase.UpdateLevelUseCase
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.viewmodel.BaseGameViewModel
import uk.fernando.memory.viewmodel.BaseViewModel
import uk.fernando.memory.viewmodel.GameViewData

class GameViewModel(
    updateLevelUseCase: UpdateLevelUseCase,
    prefsStore: PrefsStore,
    repository: LevelRepository,
    logger: MyLogger
) : BaseGameViewModel(), GameViewData {

    private val gameUseCase = GameUseCase(this, prefsStore, updateLevelUseCase, repository, logger)
    val levelResult = mutableStateOf<LevelEntity?>(null)

    suspend fun setUpGame(levelID: Int, categoryId: Int) = flow {
        emit(gameUseCase.createCardList(levelID, categoryId))
    }

    override fun startGame() {
        launchDefault {
            (0 until _cardList.size).forEach { index -> // Hide all cards after some secs
                _cardList[index] = _cardList[index].copy(status = CardFace.Front)
                delay(50)
            }
        }
    }

    override fun setSelectedCard(card: CardModel) = flow {
        val isCorrect = gameUseCase.setSelectedCard(card)
        emit(isCorrect)

        if (isCorrect != null)
            gameUseCase.updateSelectedCards(isCorrect)
    }

    override fun updateMistakes(value: Int) {
        mistakes.value += value
    }

    override fun endGame(level: LevelEntity?, score: ScoreEntity?) {
        levelResult.value = level
    }

    override fun startGame(cards: List<CardModel>) {
        _cardList.clear()
        _cardList.addAll(cards)
        quantity.value = cards.size
    }

    override fun updateCardStatus(card: CardModel, status: CardFace): CardModel {
        val index = _cardList.indexOf(card)
        _cardList[index] = _cardList[index].copy(status = status)
        return _cardList[index]
    }
}
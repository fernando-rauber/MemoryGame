package uk.fernando.memory.viewmodel.custom

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.datastore.GamePrefsStore
import uk.fernando.memory.repository.ScoreRepository
import uk.fernando.memory.usecase.CustomGameUseCase
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.viewmodel.BaseGameViewModel
import uk.fernando.memory.viewmodel.GameViewData


class CustomGameViewModel(
    private val gamePrefsStore: GamePrefsStore,
    repository: ScoreRepository,
    logger: MyLogger
) : BaseGameViewModel(), GameViewData {

    private val gameUseCase = CustomGameUseCase(this, repository, logger)
    val result = mutableStateOf<ScoreEntity?>(null)

    suspend fun setUpGame() {
        val boardSize = gamePrefsStore.getBoardSize()
        val categories = gamePrefsStore.getCategoryList()

        gameUseCase.createCardList(boardSize, categories)
    }

    override fun startGame() {
        launchDefault {
            (0 until _cardList.size).forEach { index -> // Hide all cards after some secs
                _cardList[index] = _cardList[index].copy(status = CardFace.Front)
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
        result.value = score
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
package uk.fernando.memory.usecase

import kotlinx.coroutines.delay
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.database.entity.ScoreEntity
import uk.fernando.memory.repository.ScoreRepository
import uk.fernando.memory.util.CardGenerator
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.viewmodel.GameViewData
import uk.fernando.util.ext.TAG

class CustomGameUseCase(
    private val gameData: GameViewData,
    private val repository: ScoreRepository,
    private val logger: MyLogger
) {

    private var firstCard: CardModel? = null
    private var secondCard: CardModel? = null
    private var totalCards = 0
    private var attempts = 0
    private var boardSize = 0
    private var categoryList = emptyList<Int>()

    fun createCardList(boardSize: Int, categoryList: List<Int>) {
        this.boardSize = boardSize
        this.totalCards = ((boardSize * boardSize) / 2) * 2
        this.categoryList = categoryList
        gameData.startGame(CardGenerator().generateCards(totalCards, categoryList))
    }

    private suspend fun insertScore(): ScoreEntity {
        val score = ScoreEntity(attempts = attempts, boardSize = boardSize, categoryList = categoryList)

        kotlin.runCatching {
            repository.insertHistory(score)
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error inserting score: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
        return score
    }

    fun setSelectedCard(card: CardModel): Boolean? {
        kotlin.runCatching {
            if (card.status == CardFace.Back || card.status == CardFace.Hidden || firstCard != null && secondCard != null)
                return null

            val newCard = gameData.updateCardStatus(card, CardFace.Back)

            if (firstCard == null)
                firstCard = newCard
            else if (secondCard == null)
                secondCard = newCard

            // Validate both cards
            if (firstCard != null && secondCard != null) {

                return if (firstCard!!.id != secondCard!!.id) { // Incorrect
                    attempts += 1
                    gameData.updateMistakes(1)
                    false
                } else { // Correct
                    totalCards -= 2
                    true
                }
            }

        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to flip card: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }

        return null
    }

    suspend fun updateSelectedCards(isCorrect: Boolean) {
        val status = if (isCorrect) CardFace.Hidden else CardFace.Front

        delay(600)

        gameData.updateCardStatus(firstCard!!, status)
        gameData.updateCardStatus(secondCard!!, status)

        firstCard = null
        secondCard = null

        // check if EndGame
        if (totalCards == 0)
            gameData.endGame(null, insertScore())
    }
}
package uk.fernando.memory.usecase

import kotlinx.coroutines.delay
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.ext.getStarsByAttempts
import uk.fernando.memory.util.CardGenerator
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.viewmodel.GameViewData

class GameUseCase(
    private val gameData: GameViewData,
    private val updateLevelUseCase: UpdateLevelUseCase,
    private val getLevelUseCase: GetLevelUseCase,
    private val logger: MyLogger
) {

    private lateinit var level: LevelEntity
    private var firstCard: CardModel? = null
    private var secondCard: CardModel? = null
    private var totalCards = 0
    private var attemptsLeft = 15

    suspend fun createCardList(levelID: Int, type: Int) {
        level = getLevelUseCase(levelID)
        this.totalCards = level.cardQuantity

        gameData.startGame(CardGenerator().generateCards(totalCards, type))
    }

    suspend fun updateLevel(time: Int) {
        kotlin.runCatching {
            val stars = attemptsLeft.getStarsByAttempts()
            if (stars >= level.starCount) // In case user play again same level
                updateLevelUseCase.invoke(stars, time, level.id!!)
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error update level: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    suspend fun setSelectedCard(card: CardModel) {
        if (card.status == CardFace.Back || card.status == CardFace.Hidden || firstCard != null && secondCard != null)
            return

        val newCard = gameData.updateCardStatus(card, CardFace.Back)

        if (firstCard == null)
            firstCard = newCard
        else if (firstCard != null && secondCard == null)
            secondCard = newCard

        // Validate both cards
        if (firstCard != null && secondCard != null) {
            delay(800)

            val newStatus = if (firstCard!!.id != secondCard!!.id) { // Incorrect
                attemptsLeft -= 1
                gameData.updateAttempts(1)
                CardFace.Front
            } else { // Correct
                totalCards -= 2
                CardFace.Hidden
            }

            gameData.updateCardStatus(firstCard!!, newStatus)
            gameData.updateCardStatus(secondCard!!, newStatus)

            firstCard = null
            secondCard = null

            if (attemptsLeft <= 0)  // Game over
                gameData.endGame(level.copy(starCount = 0))
            else if (totalCards == 0)
                gameData.endGame(level.copy(starCount = attemptsLeft.getStarsByAttempts()))
        }
    }
}
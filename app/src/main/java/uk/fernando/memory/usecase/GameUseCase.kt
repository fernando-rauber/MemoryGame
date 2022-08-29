package uk.fernando.memory.usecase

import kotlinx.coroutines.delay
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.ext.getStarsByMistakes
import uk.fernando.memory.util.CardGenerator
import uk.fernando.memory.util.CardModel
import uk.fernando.memory.viewmodel.GameViewData

class GameUseCase(
    private val gameData: GameViewData,
    private val prefsStore: PrefsStore,
    private val updateLevelUseCase: UpdateLevelUseCase,
    private val getLevelUseCase: GetLevelUseCase,
    private val logger: MyLogger
) {

    private lateinit var level: LevelEntity
    private var firstCard: CardModel? = null
    private var secondCard: CardModel? = null
    private var totalCards = 0
    private var mistakes = 0

    suspend fun createCardList(levelID: Int, type: Int): Boolean {
        level = getLevelUseCase(levelID)

        if (!level.isDisabled) {
            this.totalCards = level.quantity
            gameData.startGame(CardGenerator().generateCards(totalCards, type))
        }

        return level.isDisabled
    }

    private suspend fun updateLevel() {
        kotlin.runCatching {
            val stars = mistakes.getStarsByMistakes()
            if (stars > level.star) {  // In case user replay same level and score is lower then previous
                prefsStore.storeStar(stars - level.star)
                updateLevelUseCase.invoke(level.copy(star = stars, mistakes = mistakes))
            }
        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error update level: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    suspend fun setSelectedCard(card: CardModel) {
        kotlin.runCatching {
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
                    mistakes += 1
                    gameData.updateMistakes(1)
                    CardFace.Front
                } else { // Correct
                    totalCards -= 2
                    CardFace.Hidden
                }

                gameData.updateCardStatus(firstCard!!, newStatus)
                gameData.updateCardStatus(secondCard!!, newStatus)

                firstCard = null
                secondCard = null

                checkIfEndGame()
            }

        }.onFailure { e ->
            logger.e(TAG, e.message.toString())
            logger.addMessageToCrashlytics(TAG, "Error to flip card: msg: ${e.message}")
            logger.addExceptionToCrashlytics(e)
        }
    }

    private suspend fun checkIfEndGame() {
        if (mistakes >= MISTAKES_POSSIBLE) {  // Game over
            gameData.endGame(level.copy(star = 0, mistakes = mistakes))
            updateLevel()
        } else if (totalCards == 0) {
            gameData.endGame(level.copy(star = mistakes.getStarsByMistakes(), mistakes = mistakes))
            updateLevel()
        }
    }
}
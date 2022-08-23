package uk.fernando.memory.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.usecase.UpdateLevelUseCase
import uk.fernando.memory.util.CardGenerator
import uk.fernando.memory.util.CardModel
import java.util.*


class GameViewModel(private val updateLevelUseCase: UpdateLevelUseCase, private val logger: MyLogger) : BaseViewModel() {

    private var levelID: Int = 0
    private var firstCard: CardModel? = null
    private var secondCard: CardModel? = null
    val chronometerSeconds = mutableStateOf(0)

    private val _cardList = mutableStateListOf<CardModel>()
    val cardList: List<CardModel> = _cardList
    val attemptsLeft = mutableStateOf(5)
    private var totalCards = 0
    val isGameFinished = mutableStateOf(false)

    fun setUpGame(levelID: Int, cardQuantity: Int) {
        this.levelID = levelID

        _cardList.clear()
        _cardList.addAll(CardGenerator().generateCards(cardQuantity, 1))

        totalCards = _cardList.size
    }

    fun startGame() {
        // Hide all cards after some secs
        launchDefault {
            (0 until _cardList.size).forEach { index ->
                delay(150)
                _cardList[index] = _cardList[index].copy(status = CardFace.Front)
            }

            chronometer.start()
        }
    }

    fun setSelectedCard(card: CardModel) {
        if (card.status == CardFace.Back || card.status == CardFace.Hidden || firstCard != null && secondCard != null)
            return

        val newCard = updateListItem(card, CardFace.Back)

        if (firstCard == null)
            firstCard = newCard
        else if (firstCard != null && secondCard == null)
            secondCard = newCard

        // Validate both cards
        if (firstCard != null && secondCard != null) {
            launchDefault {
                delay(800)

                val newStatus = if (firstCard!!.id != secondCard!!.id) { // Incorrect
                    attemptsLeft.value -= 1
                    CardFace.Front
                } else { // Correct
                    totalCards -= 2
                    CardFace.Hidden
                }

                updateListItem(firstCard!!, newStatus)
                updateListItem(secondCard!!, newStatus)

                firstCard = null
                secondCard = null

                if (attemptsLeft.value <= 0)
                    isGameFinished.value = true

                if (totalCards == 0)
                    updateLevel()
            }
        }
    }

    private fun updateListItem(card: CardModel, status: CardFace): CardModel {
        val index = _cardList.indexOf(card)
        _cardList[index] = _cardList[index].copy(status = status)
        return _cardList[index]
    }

    fun retryOrNextLevel() {
        chronometerSeconds.value = 0
        _cardList.clear()
        attemptsLeft.value = 5
        isGameFinished.value = false

        // Retry Level
        setUpGame(levelID, totalCards)
    }

    private fun updateLevel() {
        launchDefault {
            kotlin.runCatching {
                isGameFinished.value = true

                val timer = chronometerSeconds.value
                chronometer.cancel()

                updateLevelUseCase.invoke(getStars(), timer, levelID)
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addMessageToCrashlytics(TAG, "Error update level: msg: ${e.message}")
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun getStars(): Int {
        return when (attemptsLeft.value) {
            5 -> 3
            4, 3 -> 2
            else -> 1
        }
    }

    private val chronometer = object : CountDownTimer(7200000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            chronometerSeconds.value++
        }

        override fun onFinish() {
            chronometerSeconds.value = 0
        }
    }
}
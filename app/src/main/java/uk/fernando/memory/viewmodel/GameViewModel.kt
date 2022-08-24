package uk.fernando.memory.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import uk.fernando.logger.MyLogger
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.config.AppConfig.ATTEMPTS_AVAILABLE
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.usecase.GameUseCase
import uk.fernando.memory.usecase.GetLevelUseCase
import uk.fernando.memory.usecase.UpdateLevelUseCase
import uk.fernando.memory.util.CardModel

interface GameViewData {
    fun updateAttempts(value: Int)
    fun endGame(level: LevelEntity)
    fun startGame(cards: List<CardModel>)
    fun updateCardStatus(card: CardModel, status: CardFace): CardModel
}

class GameViewModel(
    updateLevelUseCase: UpdateLevelUseCase,
    prefsStore: PrefsStore,
    getLevelUseCase: GetLevelUseCase,
    logger: MyLogger
) : BaseViewModel(), GameViewData {

    private val gameUseCase = GameUseCase(this, prefsStore, updateLevelUseCase, getLevelUseCase, logger)

    private val _cardList = mutableStateListOf<CardModel>()
    val cardList: List<CardModel> = _cardList
    val chronometerSeconds = mutableStateOf(0)
    val attemptsLeft = mutableStateOf(ATTEMPTS_AVAILABLE)
    val quantity = mutableStateOf(0)
    val levelResult = mutableStateOf<LevelEntity?>(null)

    suspend fun setUpGame(levelID: Int) = flow {
        emit(gameUseCase.createCardList(levelID, 1))
    }

    fun startGame() {
        launchDefault {
            (0 until _cardList.size).forEach { index -> // Hide all cards after some secs
                _cardList[index] = _cardList[index].copy(status = CardFace.Front)
                delay(70)
            }
            chronometer.start()
        }
    }

    fun setSelectedCard(card: CardModel) {
        launchDefault { gameUseCase.setSelectedCard(card) }
    }

    override fun updateAttempts(value: Int) {
        attemptsLeft.value -= value
    }

    override fun endGame(level: LevelEntity) {
        launchDefault {
            val time = chronometerSeconds.value
            chronometer.cancel()

            gameUseCase.updateLevel(time)

            levelResult.value = level.copy(time = time)
        }
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

    private val chronometer = object : CountDownTimer(7200000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            chronometerSeconds.value++
        }

        override fun onFinish() {
            chronometerSeconds.value = 0
        }
    }
}
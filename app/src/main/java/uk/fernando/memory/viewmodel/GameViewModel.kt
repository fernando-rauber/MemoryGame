package uk.fernando.memory.viewmodel

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.delay
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.usecase.UpdateLevelUseCase
import java.util.*

data class MyCard(
    val uuid: String = UUID.randomUUID().toString(),
    val id: Int,
    val status: CardFace = CardFace.Back
)

class GameViewModel(private val updateLevelUseCase: UpdateLevelUseCase) : BaseViewModel() {

    private var firstCard: MyCard? = null
    private var secondCard: MyCard? = null
    private val _cardList = mutableStateListOf<MyCard>()
    val cardList: List<MyCard> = _cardList

    init {
        _cardList.addAll(
            listOf(
                MyCard(id = 1),
                MyCard(id = 2),
                MyCard(id = 3),
                MyCard(id = 4),
                MyCard(id = 1),
                MyCard(id = 2),
                MyCard(id = 3),
                MyCard(id = 4),
            )
        )

        // Hide all cards after 10 secs
        launchDefault {
            delay(3000)
            (0 until _cardList.size).forEach { index ->
                delay(150)
                _cardList[index] = _cardList[index].copy(status = CardFace.Front)
            }
        }

    }

    fun setSelectedCard(card: MyCard) {
        if (card.status == CardFace.Back || card.status == CardFace.Hidden || firstCard != null && secondCard != null)
            return

        val newCard = updateListItem(card, CardFace.Back)

        if (firstCard == null)
            firstCard = newCard
        else if (firstCard != null && secondCard == null)
            secondCard = newCard

        if (firstCard != null && secondCard != null) {
            launchDefault {
                delay(800)

                val newStatus = if (firstCard!!.id != secondCard!!.id) // Incorrect
                    CardFace.Front
                else // Correct
                    CardFace.Hidden

                updateListItem(firstCard!!, newStatus)
                updateListItem(secondCard!!, newStatus)

                firstCard = null
                secondCard = null
            }
        }
    }

    private fun updateListItem(card: MyCard, status: CardFace): MyCard {
        val index = _cardList.indexOf(card)
        _cardList[index] = _cardList[index].copy(status = status)
        return _cardList[index]
    }

    fun updateLevel(stars: Int, time: Int, levelID: Int) {
        launchDefault {
            updateLevelUseCase.invoke(stars, time, levelID)
        }
    }

}
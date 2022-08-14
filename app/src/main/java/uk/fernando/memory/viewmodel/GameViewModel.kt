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

    private var cardOne: MyCard? = null
    private val _cardList = mutableStateListOf<MyCard>()
    val cardList: List<MyCard> = _cardList

    init {
        _cardList.addAll(
            listOf(
                MyCard( id=1),
                MyCard( id=2),
                MyCard( id=3),
                MyCard( id=4),
                MyCard( id=1),
                MyCard( id=2),
                MyCard( id=3),
                MyCard( id=4),
            )
        )

        launchDefault {
            delay(10000)
            (0 until _cardList.size).forEach { index ->
                delay(200)
                _cardList[index] =  _cardList[index].copy(status = CardFace.Front)
            }
        }

    }

    fun setSelectedCard(card: MyCard) {
        val newCard = updateListItem(card, CardFace.Back)

        if (cardOne == null)
            cardOne = newCard
        else if (cardOne != newCard) {
            if (newCard.id != cardOne!!.id) {// Incorrect
                launchDefault {
                    delay(800)

                    updateListItem(cardOne!!, CardFace.Front)
                    updateListItem(newCard, CardFace.Front)

                    cardOne = null
                }
            }else{ // Correct
                launchDefault {
                    delay(800)

                    updateListItem(cardOne!!, CardFace.Hide)
                    updateListItem(newCard, CardFace.Hide)

                    cardOne = null
                }
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
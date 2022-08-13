package uk.fernando.memory.viewmodel

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.delay
import uk.fernando.memory.usecase.UpdateLevelUseCase

data class MyCard(val id: Int, val hide: Boolean, var front: Boolean)

class GameViewModel(private val updateLevelUseCase: UpdateLevelUseCase) : BaseViewModel() {

    private var cardOne: MyCard? = null
    private val _cardList = mutableStateListOf<MyCard>()
    val cardList: List<MyCard> = _cardList

    init {
        _cardList.addAll(
            listOf(
                MyCard(1, false, true),
                MyCard(2, false, true),
                MyCard(3, false, true),
                MyCard(4, false, true),
            )
        )
    }

    fun setSelectedCard(card: MyCard) {
        val newCard = updateListItem(card, false)

        if (cardOne == null)
            cardOne = newCard
        else if (cardOne != newCard) {
            if (newCard.id != cardOne!!.id) {
                launchDefault {
                    delay(1000)

                    updateListItem(cardOne!!, true)
                    updateListItem(newCard, true)

                    cardOne = null
                }
            }
        }
    }

    private fun updateListItem(card: MyCard, isFront: Boolean): MyCard {
        val index = _cardList.indexOf(card)
        _cardList[index] = _cardList[index].copy(front = isFront)
        return _cardList[index]
    }

    fun updateLevel(stars: Int, time: Int, levelID: Int) {
        launchDefault {
            updateLevelUseCase.invoke(stars, time, levelID)
        }
    }

}
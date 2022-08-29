package uk.fernando.memory.util

import uk.fernando.memory.component.CardFace
import java.util.*

data class CardModel(
    val uuid: String = UUID.randomUUID().toString(),
    val id: Int,
    val type: Int,
    val status: CardFace = CardFace.BackDisabled
)
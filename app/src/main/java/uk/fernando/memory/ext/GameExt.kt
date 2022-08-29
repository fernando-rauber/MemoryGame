package uk.fernando.memory.ext

import uk.fernando.memory.R
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.util.CardType


fun Int.getWidthSize(): Float {
    return when (this) {
        in 4..8 -> 0.6f
        else -> 1f
    }
}

fun Int.getCellCount(): Int {
    return when (this) {
        in 4..8 -> 2
        12 -> 3
        else -> 4
    }
}

fun Int.getStarsByMistakes(): Int {
    return if (this < MISTAKES_POSSIBLE * .3)
        3
    else if (this < MISTAKES_POSSIBLE * .6)
        2
    else if (this < MISTAKES_POSSIBLE)
        1
    else
        0
}

fun Int.getTypeName(): Int {
    return when (CardType.getByValue(this)) {
        CardType.ANIMAL -> R.string.animal
        CardType.FLAG -> R.string.flags
        else -> R.string.numbers
    }
}


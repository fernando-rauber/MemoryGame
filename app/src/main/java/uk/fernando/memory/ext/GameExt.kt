package uk.fernando.memory.ext

import uk.fernando.memory.R
import uk.fernando.memory.config.AppConfig.ATTEMPTS_AVAILABLE
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

fun Int.getStarsByAttempts(): Int {
    return if (this > ATTEMPTS_AVAILABLE * .7)
        3
    else if (this > ATTEMPTS_AVAILABLE * .4)
        2
    else if (this > 0)
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


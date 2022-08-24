package uk.fernando.memory.ext

import uk.fernando.memory.util.CardType
import uk.fernando.memory.R


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
    return when (this) {
        in 12..15 -> 3
        in 6..11 -> 2
        in 1..5 -> 1
        else -> 0
    }
}

fun Int.getTypeName(): Int {
    return when (CardType.getByValue(this)) {
        CardType.ANIMAL -> R.string.animal
        CardType.FLAG -> R.string.flags
        else -> R.string.numbers
    }
}


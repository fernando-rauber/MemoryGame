package uk.fernando.memory.ext

import androidx.compose.ui.graphics.Color
import uk.fernando.memory.R
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.theme.*
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
        30 -> 5
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

fun Int.getBackgroundColor(): Color {
    return when (this.toString().last()) {
        '1' -> pastelGreen
        '2' -> pastelOrange
        '3' -> pastelPink
        '4' -> pastelRed
        '5', '6' -> pastelPurple
        '7' -> pastelYellow
        '8' -> pastelBlue
        else -> pastelGreenDark
    }
}

fun Int.getTypeName(): Int {
    return when (CardType.getByValue(this)) {
        CardType.ANIMAL -> R.string.animal
        CardType.FLAG -> R.string.flags
        CardType.TREE -> R.string.trees
        CardType.FOOD -> R.string.foods
        CardType.TILE -> R.string.tiles
        else -> R.string.numbers
    }
}


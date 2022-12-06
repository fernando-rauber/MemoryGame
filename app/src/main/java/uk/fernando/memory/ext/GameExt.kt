package uk.fernando.memory.ext

import androidx.compose.ui.graphics.Color
import uk.fernando.memory.R
import uk.fernando.memory.config.AppConfig.MISTAKES_POSSIBLE
import uk.fernando.memory.config.AppConfig.SCREEN_HEIGHT
import uk.fernando.memory.theme.*
import uk.fernando.memory.util.CardType
import uk.fernando.memory.util.CardType.Companion.getByValue


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
        24 -> if(SCREEN_HEIGHT > 700) 4 else 5
        28, 30 -> 5
        34, 38, 40 -> if(SCREEN_HEIGHT > 700) 5 else 6
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

fun Int.getCategoryIcon(): Int {
    return when (getByValue(this)) {
        CardType.ANIMAL -> R.drawable.animal_bee
        CardType.FLAG -> R.drawable.flag_australia
        CardType.TREE -> R.drawable.tree_apricot
        CardType.FOOD -> R.drawable.food_avocado
        CardType.TILE -> R.drawable.tile_11
        else -> R.drawable.tile_11
    }
}

fun Int.getCategoryName(): Int {
    return when (CardType.getByValue(this)) {
        CardType.ANIMAL -> R.string.animal
        CardType.FLAG -> R.string.flags
        CardType.TREE -> R.string.trees
        CardType.FOOD -> R.string.foods
        CardType.TILE -> R.string.tiles
        else -> R.string.numbers
    }
}


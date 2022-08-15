package uk.fernando.memory.ext


fun Int.getWidthSize(): Float {
    return when (this) {
        4, 6, 8 -> 0.6f
        else -> 0f
    }
}

fun Int.getCellCount(): Int {
    return when (this) {
        4, 6, 8 -> 2
        12 -> 3
        else -> 4
    }
}

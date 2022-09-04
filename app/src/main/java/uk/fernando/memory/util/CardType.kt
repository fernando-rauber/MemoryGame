package uk.fernando.memory.util

enum class CardType(val value: Int) {
    ANIMAL(1),
    NUMBER(2),
    FLAG(3),
    FOOD(4),
    OBJECTS(5);

    companion object {
        fun getByValue(value: Int) = values().firstOrNull { it.value == value }
        fun getQuantity() = values().size
    }
}
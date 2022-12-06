package uk.fernando.memory.database.converter

import androidx.room.TypeConverter
import java.util.*

const val SEPARATOR = "*"
class ListTypeConverter {

    @TypeConverter
    fun serializeList(list: List<Int>) : String =
        list.map { it }.joinToString(separator = SEPARATOR)

    @TypeConverter
    fun deserializeList(value: String): List<Int> {
        return value.split(SEPARATOR).map { it.toInt()}
    }
}
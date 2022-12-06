package uk.fernando.memory.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = ScoreEntity.NAME)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val date: Date = Date(),
    var timer: Int = 0,
    var attempts: Int = 0,

    @ColumnInfo(name = "board_size")
    var boardSize: Int = 0,

    @ColumnInfo(name = "categories")
    var categoryList: List<Int> = emptyList(),

    var multiplayer: Boolean = false

) : Serializable {

    companion object {
        const val NAME = "score"
    }
}

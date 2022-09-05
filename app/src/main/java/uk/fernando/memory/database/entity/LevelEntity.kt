package uk.fernando.memory.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.io.Serializable

@Entity(
    tableName = LevelEntity.NAME,
    primaryKeys = ["id", "category_id"],
    foreignKeys = [ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["category_id"])]
)
data class LevelEntity(
    val id: Int,

    @ColumnInfo(name = "is_disabled")
    val isDisabled: Boolean = true,

    val quantity: Int,
    val star: Int = 0,
    val mistakes: Int = 0,

    @ColumnInfo(name = "category_id", index = true)
    var categoryID: Int = 0
) : Serializable {

    companion object {
        const val NAME = "level"
    }
}

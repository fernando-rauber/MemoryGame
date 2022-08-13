package uk.fernando.memory.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = LevelEntity.NAME,
    foreignKeys = [ForeignKey(entity = MapEntity::class, parentColumns = ["id"], childColumns = ["map_id"])]
)
data class LevelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val position: Int,

    @ColumnInfo(name = "is_disabled")
    val isDisabled: Boolean = true,

    @ColumnInfo(name = "star_count")
    val starCount: Int = 0,

    val time: Int = 0,

    @ColumnInfo(name = "map_id", index = true)
    var mapID: Int = 0
) : Serializable {

    companion object {
        const val NAME = "level"
    }
}

package uk.fernando.memory.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = MapEntity.NAME)
data class MapEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
) : Serializable {

    companion object {
        const val NAME = "map"
    }
}

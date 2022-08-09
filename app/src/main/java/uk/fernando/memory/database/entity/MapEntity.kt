package uk.fernando.memory.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = MapEntity.NAME)
data class MapEntity(
    @PrimaryKey
    val id: Int,
) : Serializable {

    companion object {
        const val NAME = "map"
    }
}

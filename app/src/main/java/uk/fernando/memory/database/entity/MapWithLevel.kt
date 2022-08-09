package uk.fernando.memory.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


data class MapWithLevel(

    @Embedded val map: MapEntity,

    @Relation(parentColumn = "id", entityColumn = "map_id", entity = LevelEntity::class)
    val levelList: List<LevelEntity> = listOf()

) : Serializable
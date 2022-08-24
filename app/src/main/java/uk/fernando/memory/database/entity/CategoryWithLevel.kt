package uk.fernando.memory.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable


data class CategoryWithLevel(

    @Embedded val category: CategoryEntity,

    @Relation(parentColumn = "id", entityColumn = "category_id", entity = LevelEntity::class)
    val levelList: List<LevelEntity> = listOf()

) : Serializable
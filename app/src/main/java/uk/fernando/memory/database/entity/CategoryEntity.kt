package uk.fernando.memory.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = CategoryEntity.NAME)
data class CategoryEntity(
    @PrimaryKey
    val id: Int,

    val type: Int,

    @ColumnInfo(name = "stars_required")
    val starsRequired: Int,
) : Serializable {

    companion object {
        const val NAME = "category"
    }
}

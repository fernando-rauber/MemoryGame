package uk.fernando.memory.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.MapEntity

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(level: LevelEntity)

}
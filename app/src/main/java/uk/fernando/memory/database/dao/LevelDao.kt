package uk.fernando.memory.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.fernando.memory.database.entity.LevelEntity

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<LevelEntity>)

    @Query("UPDATE ${LevelEntity.NAME} SET is_disabled = 0 WHERE id = :levelID")
    fun enableLevel(levelID: Int)

    @Query("UPDATE ${LevelEntity.NAME} SET star_count = :stars, time = :time WHERE id = :levelID")
    fun updateLevel(stars: Int, time: Int, levelID: Int)
}
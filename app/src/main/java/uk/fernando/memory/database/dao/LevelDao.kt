package uk.fernando.memory.database.dao

import androidx.room.*
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.MapEntity

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<LevelEntity>)

    @Query("UPDATE ${LevelEntity.NAME} SET is_disabled = 0 WHERE id = :levelID")
    fun enableLevel(levelID: Int)

    @Update
    fun update(level: LevelEntity)

    @Query("SELECT * FROM ${LevelEntity.NAME} WHERE id = :ID")
    fun getLevelById(ID: Int) : LevelEntity
}
package uk.fernando.memory.database.dao

import androidx.room.*
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.LevelEntity

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

    @Query("SELECT type FROM ${CategoryEntity.NAME} WHERE id = :ID")
    fun getCardTypeByCategory(ID: Int) : Int
}
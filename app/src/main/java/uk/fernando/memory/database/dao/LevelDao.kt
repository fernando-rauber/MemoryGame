package uk.fernando.memory.database.dao

import androidx.room.*
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.LevelEntity

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<LevelEntity>)

    @Query("UPDATE ${LevelEntity.NAME} SET is_disabled = 0 WHERE id = :levelID AND category_id = :categoryId")
    fun enableLevel(levelID: Int, categoryId: Int)

    @Update
    fun update(level: LevelEntity)

    @Delete
    fun delete(level: LevelEntity)

    @Query("SELECT * FROM ${LevelEntity.NAME} WHERE id = :ID AND category_id = :categoryId")
    fun getLevelById(ID: Int, categoryId: Int) : LevelEntity

    @Query("SELECT type FROM ${CategoryEntity.NAME} WHERE id = :ID")
    fun getCardTypeByCategory(ID: Int) : Int
}
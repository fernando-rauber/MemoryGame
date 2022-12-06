package uk.fernando.memory.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import uk.fernando.memory.database.entity.ScoreEntity

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ScoreEntity): Long

    @Transaction
    @Query("SELECT * FROM ${ScoreEntity.NAME} WHERE multiplayer = :isMultiplayer ORDER BY date DESC")
    fun getHistory(isMultiplayer: Boolean): PagingSource<Int, ScoreEntity>

}
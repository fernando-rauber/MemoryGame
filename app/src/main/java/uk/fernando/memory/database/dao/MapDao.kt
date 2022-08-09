package uk.fernando.memory.database.dao

import androidx.room.*
import uk.fernando.memory.database.entity.MapEntity
import uk.fernando.memory.database.entity.MapWithLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(map: MapEntity)

    @Query("SELECT * FROM ${MapEntity.NAME} ORDER by id")
    fun getMapAndLevelList(): Flow<List<MapWithLevel>>
}
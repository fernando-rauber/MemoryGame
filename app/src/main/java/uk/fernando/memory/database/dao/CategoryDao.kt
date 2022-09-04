package uk.fernando.memory.database.dao

import androidx.room.*
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.CategoryWithLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM ${CategoryEntity.NAME} ORDER by id")
    fun getCategoryWithLevelList(): Flow<List<CategoryWithLevel>>
}
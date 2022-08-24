package uk.fernando.memory.repository

import kotlinx.coroutines.flow.Flow
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.CategoryWithLevel


interface CategoryRepository {

    suspend fun getCategoryWithLevelList(): Flow<List<CategoryWithLevel>>

    suspend fun insert(category: CategoryEntity)

}
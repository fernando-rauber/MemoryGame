package uk.fernando.memory.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.CategoryWithLevel
import uk.fernando.memory.database.entity.LevelEntity

open class CategoryRepositoryMock : CategoryRepository {

    override suspend fun getCategoryWithLevelList(): Flow<List<CategoryWithLevel>> {
        val listLevel = listOf(LevelEntity(1, quantity = 4, categoryID = 1, isDisabled = false), LevelEntity(2, quantity = 6, categoryID = 1))
        val category = CategoryEntity(1, 1, 10)
        val categoryWithLevel = listOf( CategoryWithLevel(category, listLevel))

        return listOf(categoryWithLevel).asFlow()
    }

    override suspend fun insert(category: CategoryEntity) {
        TODO("Not yet implemented")
    }

}


package uk.fernando.memory.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.memory.database.dao.CategoryDao
import uk.fernando.memory.database.entity.CategoryEntity


open class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {

    override suspend fun getCategoryWithLevelList() = withContext(Dispatchers.IO) {
        dao.getCategoryWithLevelList()
    }

    override suspend fun insert(category: CategoryEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(category)
        }
    }
}

package uk.fernando.memory.repository

import uk.fernando.memory.database.entity.LevelEntity

open class LevelRepositoryMock : LevelRepository {

    private val leveList = listOf(
        LevelEntity(1, quantity = 4, categoryID = 1, isDisabled = false),
        LevelEntity(2, quantity = 4, categoryID = 1),
        LevelEntity(3, quantity = 4, categoryID = 1),
        LevelEntity(5, quantity = 4, categoryID = 1)
    )

    override suspend fun insert(list: List<LevelEntity>) {}

    override suspend fun enableLevel(levelID: Int, categoryId: Int) {}

    override suspend fun update(level: LevelEntity) {}

    override suspend fun getLevelById(id: Int, categoryId: Int): LevelEntity {
        return leveList.first { it.id == id }
    }

    override suspend fun getCardTypeByCategory(id: Int): Int {
        return 1
    }
}


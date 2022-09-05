package uk.fernando.memory.repository

import uk.fernando.memory.database.entity.LevelEntity

interface LevelRepository {

    suspend fun insert(list: List<LevelEntity>)

    suspend fun enableLevel(levelID: Int, categoryId: Int)

    suspend fun update(level: LevelEntity)

    suspend fun getLevelById(id: Int, categoryId: Int): LevelEntity

    suspend fun getCardTypeByCategory(id: Int): Int

}
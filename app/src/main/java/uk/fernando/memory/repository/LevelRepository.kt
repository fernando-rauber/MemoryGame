package uk.fernando.memory.repository

import uk.fernando.memory.database.entity.LevelEntity


interface LevelRepository {

    suspend fun insert(list: List<LevelEntity>)

}
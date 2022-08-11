package uk.fernando.memory.repository

import uk.fernando.memory.database.entity.LevelEntity


interface LevelRepository {

    suspend fun insert(list: List<LevelEntity>)

    suspend fun enableLevel(levelID: Int)

    suspend fun updateLevel(stars: Int, time: Int, levelID: Int)

}
package uk.fernando.memory.repository

import kotlinx.coroutines.flow.Flow
import uk.fernando.memory.database.entity.MapEntity
import uk.fernando.memory.database.entity.MapWithLevel


interface MapRepository {

    suspend fun getMapAndLevelList(): Flow<List<MapWithLevel>>

    suspend fun insert(map: MapEntity)

}
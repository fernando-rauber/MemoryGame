package uk.fernando.memory.repository

import androidx.paging.PagingSource
import uk.fernando.memory.database.entity.ScoreEntity

interface ScoreRepository {

    suspend fun insertHistory(history: ScoreEntity) : Int
    fun getAllHistory(isMultiplayer: Boolean) : PagingSource<Int, ScoreEntity>
}
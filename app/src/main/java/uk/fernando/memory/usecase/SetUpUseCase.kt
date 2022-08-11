package uk.fernando.memory.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.logger.MyLogger
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.MapEntity
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.repository.MapRepository

class SetUpUseCase(
    private val mapRepo: MapRepository,
    private val levelRepo: LevelRepository,
    private val logger: MyLogger
) {

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            runCatching {
                createLevel1()
                createLevel2()
                createLevel3()

                // Enable First Level
                levelRepo.enableLevel(1)
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private suspend fun createLevel1() {
        val map = MapEntity(1)
        mapRepo.insert(map)
        levelRepo.insert(createLevelsByType(1, 1))
    }

    private suspend fun createLevel2() {
        val map = MapEntity(2)
        mapRepo.insert(map)
        levelRepo.insert(createLevelsByType(map.id, 2))
    }

    private suspend fun createLevel3() {
        val map = MapEntity(3)
        mapRepo.insert(map)
        levelRepo.insert(createLevelsByType(map.id, 3))
    }

    private fun createLevelsByType(mapID: Int, type: Int): List<LevelEntity> {
        val levelList = mutableListOf<LevelEntity>()

        (1..30).forEach { position ->
            levelList.add(LevelEntity(position = position, mapID = mapID))
        }

        return levelList
    }
}
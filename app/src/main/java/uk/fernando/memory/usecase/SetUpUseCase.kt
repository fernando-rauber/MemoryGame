package uk.fernando.memory.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.logger.MyLogger
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_MAP
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.MapEntity
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.repository.MapRepository
import uk.fernando.memory.util.CardType

class SetUpUseCase(
    private val mapRepo: MapRepository,
    private val levelRepo: LevelRepository,
    private val logger: MyLogger
) {

    private val categoryList = listOf(CardType.ANIMAL, CardType.FLAG, CardType.NUMBER)

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            runCatching {

                categoryList.forEachIndexed { index, cardType ->
                    val map = MapEntity(
                        id = index + 1,
                        type = cardType.value,
                        starsRequired = (index * MAX_CARDS_PER_MAP * 1.9).toInt()
                    )
                    mapRepo.insert(map)
                    levelRepo.insert(createLevelsByType(map.id))
                }

                // Enable First Level
                levelRepo.enableLevel(1)
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun createLevelsByType(mapID: Int): List<LevelEntity> {
        val levelList = mutableListOf<LevelEntity>()

        (1..MAX_CARDS_PER_MAP).forEach { position ->
            val quantity = when (position) {
                1 -> 4
                2 -> 6
                3 -> 8
                4 -> 12
                else -> 20
            }

            levelList.add(LevelEntity(position = position, quantity = quantity, mapID = mapID))
        }

        return levelList
    }
}
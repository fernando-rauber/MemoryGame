package uk.fernando.memory.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.fernando.logger.MyLogger
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.STAR_REQUIRE_MULTIPLAYER
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.repository.CategoryRepository
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.util.CardType.*

class SetUpUseCase(
    private val mapRepo: CategoryRepository,
    private val levelRepo: LevelRepository,
    private val logger: MyLogger
) {

    private val categoryList = listOf(ANIMAL, FLAG, FOOD, NUMBER, TREE, TILE)

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            runCatching {

                categoryList.forEachIndexed { index, cardType ->
                    val map = CategoryEntity(
                        id = index + 1,
                        type = cardType.value,
                        starsRequired = (index * MAX_CARDS_PER_CATEGORY * STAR_REQUIRE_MULTIPLAYER).toInt()
                    )
                    mapRepo.insert(map)
                    levelRepo.insert(createLevelsByType(map.id))
                }

                // Enable First Level
                levelRepo.enableLevel(1, 1)
            }.onFailure { e ->
                logger.e(TAG, e.message.toString())
                logger.addExceptionToCrashlytics(e)
            }
        }
    }

    private fun createLevelsByType(mapID: Int): List<LevelEntity> {
        val levelList = mutableListOf<LevelEntity>()

        (1..MAX_CARDS_PER_CATEGORY).forEach { id ->
            val quantity = when (id) {
                1 -> 4
                2 -> 6
                3 -> 8
                4 -> 12
                19, 20 -> 30
                else -> 20
            }

            levelList.add(LevelEntity(id = id, quantity = quantity, categoryID = mapID))
        }

        return levelList
    }
}
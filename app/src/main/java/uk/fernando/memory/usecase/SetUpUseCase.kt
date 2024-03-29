package uk.fernando.memory.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import uk.fernando.logger.MyLogger
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.STAR_REQUIRE_MULTIPLAYER
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.repository.CategoryRepository
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.util.CardType.*
import uk.fernando.util.ext.TAG

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
                5 -> 16
                6 -> 20
                7 -> 24
                8 -> 28
                9 -> 30
                10 -> 34
                11 -> 38
                else -> 40
            }

            levelList.add(LevelEntity(id = id, quantity = quantity, categoryID = mapID))
        }

        return levelList
    }

    suspend fun updateVersion2() {
        withContext(Dispatchers.IO) {
            runCatching {

                val categoryWithList = mapRepo.getCategoryWithLevelList().first()

                categoryWithList.forEach { category ->
                    category.levelList.forEach { level ->

                        if (level.id > 12)
                            levelRepo.delete(level)
                        else if(level.id in 5..12){
                            val newQuantity = when (level.id) {
                                5 -> 16
                                6 -> 20
                                7 -> 24
                                8 -> 28
                                9 -> 30
                                10 -> 34
                                11 -> 38
                                else -> 40
                            }
                            levelRepo.update(level.copy(quantity = newQuantity))
                        }
                    }
                }
            }
        }
    }
}
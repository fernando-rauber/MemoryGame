package uk.fernando.memory.usecase

import kotlinx.coroutines.flow.first
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.STAR_REQUIRE_MULTIPLAYER
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.util.CardType

class UpdateLevelUseCase(private val prefsStore: PrefsStore, private val repository: LevelRepository) {

    suspend operator fun invoke(level: LevelEntity) {
        repository.update(level)

        val totalStars = prefsStore.getStarCount().first()

        for (categoryID in 1 until CardType.getQuantity()) {
            val starsRequiredByCategory = (categoryID * MAX_CARDS_PER_CATEGORY * STAR_REQUIRE_MULTIPLAYER).toInt()

            if (totalStars >= starsRequiredByCategory && totalStars < starsRequiredByCategory + 3)
                repository.enableLevel(1, categoryID) // Unlock first card of the category
        }

        if (level.id + 1 <= MAX_CARDS_PER_CATEGORY) {
            val categoryID = if (level.id + 1 > MAX_CARDS_PER_CATEGORY) level.categoryID + 1 else level.categoryID
            repository.enableLevel(level.id + 1, categoryID) // Enable next level
        }
    }
}
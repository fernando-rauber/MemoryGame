package uk.fernando.memory.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import uk.fernando.memory.config.AppConfig.MAX_CARDS_PER_CATEGORY
import uk.fernando.memory.config.AppConfig.STAR_REQUIRE_MULTIPLAYER
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.ext.TAG
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.util.CardType

class UpdateLevelUseCase(private val prefsStore: PrefsStore, private val repository: LevelRepository) {

    suspend operator fun invoke(level: LevelEntity) {
        repository.update(level)

        val nextCategoryID = mutableListOf<Int>()
        val totalStars = prefsStore.getStarCount().first()

        for (index in 1 until CardType.getQuantity()) {
            val firstLevel = index * MAX_CARDS_PER_CATEGORY + 1

            val starsRequiredByCategory = (index * MAX_CARDS_PER_CATEGORY * STAR_REQUIRE_MULTIPLAYER).toInt()

            if (totalStars >= starsRequiredByCategory && totalStars < starsRequiredByCategory + 3) {
                Log.e(TAG, "Unlock first card of the category : ", )
                repository.enableLevel(firstLevel) // Unlock first card of the category
            }

            nextCategoryID.add(firstLevel)
        }

        if (level.id!! + 1 !in nextCategoryID) {
            Log.e(TAG, "Enable next level ", )
            repository.enableLevel(level.id + 1) // Enable next level
        }

        // when user have all stars and win last level
        // when user win last level but not have enough stars
    }

}
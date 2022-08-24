package uk.fernando.memory.usecase

import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.repository.LevelRepository

class UpdateLevelUseCase(private val repository: LevelRepository) {

    suspend operator fun invoke(level: LevelEntity) {
        repository.update(level)
        repository.enableLevel(level.id!! + 1) // Enable next level
    }

}
package uk.fernando.memory.usecase

import uk.fernando.memory.repository.LevelRepository

class UpdateLevelUseCase(private val repository: LevelRepository) {

    suspend operator fun invoke(stars: Int, time: Int, levelID: Int) {
        repository.updateLevel(stars, time, levelID)
        repository.enableLevel(levelID + 1) // Enable next level
    }

}
package uk.fernando.memory.usecase

import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.repository.LevelRepository

class GetLevelUseCase(private val repository: LevelRepository) {

    suspend operator fun invoke(id: Int): LevelEntity {
        return repository.getLevelById(id)
    }

}
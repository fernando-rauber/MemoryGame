package uk.fernando.memory.usecase

import uk.fernando.memory.repository.MapRepository

class GetMapListUseCase(private val repository: MapRepository) {

    suspend operator fun invoke() = repository.getMapAndLevelList()

}
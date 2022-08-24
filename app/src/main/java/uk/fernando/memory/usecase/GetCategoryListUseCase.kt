package uk.fernando.memory.usecase

import uk.fernando.memory.repository.CategoryRepository

class GetCategoryListUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke() = repository.getCategoryWithLevelList()

}
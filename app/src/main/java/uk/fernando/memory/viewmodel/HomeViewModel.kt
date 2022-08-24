package uk.fernando.memory.viewmodel

import androidx.compose.runtime.mutableStateOf
import uk.fernando.memory.database.entity.CategoryWithLevel
import uk.fernando.memory.usecase.GetCategoryListUseCase

class HomeViewModel(
    private val getCategoryListUseCase: GetCategoryListUseCase,
) : BaseViewModel() {

    val categoryList = mutableStateOf(emptyList<CategoryWithLevel>())

    init {
        launchDefault {
            getCategoryListUseCase.invoke().collect() { list ->
                categoryList.value = list
            }
        }
    }

}
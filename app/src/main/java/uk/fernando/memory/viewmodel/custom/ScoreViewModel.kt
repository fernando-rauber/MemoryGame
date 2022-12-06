package uk.fernando.memory.viewmodel.custom

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import uk.fernando.memory.repository.ScoreRepository
import uk.fernando.memory.viewmodel.BaseViewModel


class ScoreViewModel(private val rep: ScoreRepository) : BaseViewModel() {

    val score = Pager(PagingConfig(10)) { rep.getAllHistory(isMultiplayer = false) }
        .flow
        .cachedIn(viewModelScope)

}
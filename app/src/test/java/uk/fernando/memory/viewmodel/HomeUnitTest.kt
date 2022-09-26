package uk.fernando.memory.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import org.koin.test.inject
import uk.fernando.memory.KoinTestCase
import uk.fernando.memory.MainCoroutineRule
import uk.fernando.memory.di.allMockedModules
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeUnitTest : KoinTestCase() {
    private val homeViewModel: HomeViewModel by inject()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    override val koinRule = KoinTestRule.create {
        modules(allMockedModules())
    }

    @Test
    fun `fetch categories and levels`() {

        assertEquals(true, homeViewModel.categoryList.value.isNotEmpty())

    }

    @Test
    fun `check if first level is unlocked`() {

        assertEquals(true, homeViewModel.categoryList.value.isNotEmpty())

        val firstLevel = homeViewModel.categoryList.value.first().levelList.first()
        assertEquals(false, firstLevel.isDisabled)
    }

}
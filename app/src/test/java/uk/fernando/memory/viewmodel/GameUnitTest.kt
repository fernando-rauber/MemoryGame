package uk.fernando.memory.viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import org.koin.test.inject
import uk.fernando.memory.KoinTestCase
import uk.fernando.memory.component.CardFace
import uk.fernando.memory.di.allMockedModules
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GameUnitTest : KoinTestCase() {
    private val gameViewModel: GameViewModel by inject()

    @get:Rule
    override val koinRule = KoinTestRule.create {
        modules(allMockedModules())
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `check amount cards created`() {
        runBlocking {
            gameViewModel.setUpGame(1, 1).collect { isLevelLocked ->

                delay(1_000)

                assertEquals(false, isLevelLocked)
                assertEquals(4, gameViewModel.quantity.value)
            }
        }
    }

    @Test
    fun `check if level is locked`() {
        runBlocking {
            gameViewModel.setUpGame(2, 1).collect { isLevelLocked ->

                delay(1_000)

                assertEquals(true, isLevelLocked)
            }
        }
    }

    @Test
    fun `check if cards flipped after countdown`() {
        runBlocking {
            gameViewModel.setUpGame(1, 1).collect {

                delay(1_000)

                assertEquals(4, gameViewModel.cardList.filter { it.status == CardFace.BackDisabled }.size)

                gameViewModel.startGame()

                delay(1_000)

                assertEquals(4, gameViewModel.cardList.filter { it.status == CardFace.Front }.size)
            }
        }
    }

    @Test
    fun `check if cards flips when selected`() {
        runBlocking {
            gameViewModel.setUpGame(1, 1).collect { isLevelLocked ->

                gameViewModel.startGame()

                delay(1_000)

                gameViewModel.setSelectedCard(gameViewModel.cardList[0]).collect { isCorrect ->
                    assertEquals(null, isCorrect)
                }

                delay(2_000)

                assertEquals(false, isLevelLocked)
                assertEquals(4, gameViewModel.quantity.value)
                assertEquals(1, gameViewModel.cardList.filter { it.status == CardFace.Back }.size)
            }
        }
    }

    @Test
    fun `check mistake increase when cards wrong selected`() {
        runBlocking {
            gameViewModel.setUpGame(1, 1).collect {

                gameViewModel.startGame()

                delay(1_000)

                val firstCard = gameViewModel.cardList[0]
                val secondCard = gameViewModel.cardList.first { it.id != firstCard.id }

                gameViewModel.setSelectedCard(firstCard).collect { isCorrect ->
                    assertEquals(null, isCorrect)
                }
                gameViewModel.setSelectedCard(secondCard).collect { isCorrect ->
                    assertEquals(false, isCorrect)
                }

                delay(1_000)

                assertEquals(1, gameViewModel.mistakes.value)
            }
        }
    }

    @Test
    fun `check card hide when get correct`() {
        runBlocking {
            gameViewModel.setUpGame(1, 1).collect {

                gameViewModel.startGame()

                delay(1_000)

                val firstCard = gameViewModel.cardList[0]
                val secondCard = gameViewModel.cardList.first { it.id == firstCard.id && it.uuid != firstCard.uuid }

                gameViewModel.setSelectedCard(firstCard).collect { isCorrect ->
                    assertEquals(null, isCorrect)
                }
                gameViewModel.setSelectedCard(secondCard).collect { isCorrect ->
                    assertEquals(true, isCorrect)
                }

                delay(1_000)

                assertEquals(2, gameViewModel.cardList.filter { it.status == CardFace.Hidden }.size)
            }
        }
    }
}
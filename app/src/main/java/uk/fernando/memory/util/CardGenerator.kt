package uk.fernando.memory.util

import uk.fernando.memory.util.CardType.*
import uk.fernando.memory.util.CardType.Companion.getByValue

class CardGenerator {

    private val numberIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }
    private val flagIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }
    private val animalIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }

    fun generateCards(quantity: Int, type: Int): List<CardModel> {
        val questionList = mutableListOf<CardModel>()

        for (i in 1..quantity / 2) {
            val idCreated = createCardId(type)

            questionList.add(CardModel(id = idCreated))
            questionList.add(CardModel(id = idCreated))
        }

        return questionList.shuffled()
    }

    /**
     * Returns a card id based on the type.
     */
    private fun createCardId(type: Int): Int {
        return when (getByValue(type)) {
            NUMBER -> getIdByTypeList(numberIds)
            ANIMAL -> getIdByTypeList(animalIds)
            FLAG -> getIdByTypeList(flagIds)
            else -> getIdByTypeList(numberIds)
        }
    }

    private fun getIdByTypeList(list: MutableList<Int>): Int {
        val value = list.first()

        list.remove(value)

        return value
    }
}

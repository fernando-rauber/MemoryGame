package uk.fernando.memory.util

import uk.fernando.memory.util.CardType.*
import uk.fernando.memory.util.CardType.Companion.getByValue
import uk.fernando.memory.R

class CardGenerator {

    private val numberIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }
    private val flagIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1225, 1296, 1369, 1521, 1600, 1681, 1764, 1936, 2116, 2209, 2401, 2500, 2601, 2704, 3249, 4225, 4356, 4489, 5625, 5776, 5929, 7225, 8100, 8281, 9216, 9409, 9604, 9801)
    }
    private val animalIds by lazy {
        mutableListOf(
            R.drawable.animal_bee,
            R.drawable.animal_cat,
            R.drawable.animal_crab,
            R.drawable.animal_dog,
            R.drawable.animal_dolphin,
            R.drawable.animal_elephant,
            R.drawable.animal_frog,
            R.drawable.animal_giraffe,
            R.drawable.animal_hen,
            R.drawable.animal_koala,
            R.drawable.animal_lion,
            R.drawable.animal_meerkat,
            R.drawable.animal_monkey,
            R.drawable.animal_panda_bear,
            R.drawable.animal_pig,
            R.drawable.animal_rabbit,
            R.drawable.animal_squirrel,
            R.drawable.animal_toucan
        )
    }

    fun generateCards(quantity: Int, type: Int): List<CardModel> {
        val questionList = mutableListOf<CardModel>()

        for (i in 1..quantity / 2) {
            val idCreated = createCardId(type)

            questionList.add(CardModel(id = idCreated, type = type))
            questionList.add(CardModel(id = idCreated, type = type))
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
        val value = list.shuffled().first()

        list.remove(value)

        return value
    }
}

package uk.fernando.memory.util

import uk.fernando.memory.R
import uk.fernando.memory.util.CardType.*
import uk.fernando.memory.util.CardType.Companion.getByValue

class CardGenerator {

    private val numberIds by lazy {
        mutableListOf(1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 361, 400, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961)
    }
    private val flagIds by lazy {
        mutableListOf(
            R.drawable.flag_argentina,
            R.drawable.flag_australia,
            R.drawable.flag_belgium,
            R.drawable.flag_bolivia,
            R.drawable.flag_brazil,
            R.drawable.flag_canada,
            R.drawable.flag_colombia,
            R.drawable.flag_costa_rica,
            R.drawable.flag_czech_republic,
            R.drawable.flag_ecuador,
            R.drawable.flag_france,
            R.drawable.flag_germany,
            R.drawable.flag_india,
            R.drawable.flag_ireland,
            R.drawable.flag_japan,
            R.drawable.flag_mexico,
            R.drawable.flag_netherlands,
            R.drawable.flag_philippines,
            R.drawable.flag_saudi_arabia,
            R.drawable.flag_south_africa,
            R.drawable.flag_south_korea,
            R.drawable.flag_spain,
            R.drawable.flag_turkey,
            R.drawable.flag_ukraine,
            R.drawable.flag_united_kingdom,
            R.drawable.flag_united_states,
            R.drawable.flag_uruguay,
            R.drawable.flag_vietnam
        )
    }
    private val animalIds by lazy {
        mutableListOf(
            R.drawable.animal_bee,
            R.drawable.animal_cat,
            R.drawable.animal_chameleon,
            R.drawable.animal_crab,
            R.drawable.animal_dog,
            R.drawable.animal_dolphin,
            R.drawable.animal_elephant,
            R.drawable.animal_frog,
            R.drawable.animal_giraffe,
            R.drawable.animal_hen,
            R.drawable.animal_horse,
            R.drawable.animal_kangaroo,
            R.drawable.animal_koala,
            R.drawable.animal_lion,
            R.drawable.animal_meerkat,
            R.drawable.animal_monkey,
            R.drawable.animal_owl,
            R.drawable.animal_panda_bear,
            R.drawable.animal_penguin,
            R.drawable.animal_pig,
            R.drawable.animal_rabbit,
            R.drawable.animal_squirrel,
            R.drawable.animal_toucan,
            R.drawable.animal_turtle,
            R.drawable.animal_walrus,
            R.drawable.animal_whale
        )
    }

    private val foodIds by lazy {
        mutableListOf(
            R.drawable.food_apple,
            R.drawable.food_avocado,
            R.drawable.food_bacon,
            R.drawable.food_bread,
            R.drawable.food_burger,
            R.drawable.food_cake,
            R.drawable.food_carrot,
            R.drawable.food_cheese,
            R.drawable.food_cherry,
            R.drawable.food_cookie,
            R.drawable.food_cucumber,
            R.drawable.food_curry,
            R.drawable.food_doughnut,
            R.drawable.food_eggplant,
            R.drawable.food_french_fries,
            R.drawable.food_grapes,
            R.drawable.food_hot_dog,
            R.drawable.food_ice_cream,
            R.drawable.food_noodle,
            R.drawable.food_pancake,
            R.drawable.food_pear,
            R.drawable.food_piece_of_cake,
            R.drawable.food_pizza,
            R.drawable.food_sandwich,
            R.drawable.food_strawberry,
            R.drawable.food_sushi,
            R.drawable.food_watermelon
        )
    }

    private val treeIds by lazy {
        mutableListOf(
            R.drawable.tree_almond,
            R.drawable.tree_apple,
            R.drawable.tree_apricot,
            R.drawable.tree_ash,
            R.drawable.tree_banana,
            R.drawable.tree_baobab,
            R.drawable.tree_beech,
            R.drawable.tree_bergamot,
            R.drawable.tree_birch,
            R.drawable.tree_cedar,
            R.drawable.tree_cherry,
            R.drawable.tree_eucalyptus,
            R.drawable.tree_joshua,
            R.drawable.tree_juniper,
            R.drawable.tree_kalleri_pear,
            R.drawable.tree_lilac,
            R.drawable.tree_oak,
            R.drawable.tree_olive,
            R.drawable.tree_orange,
            R.drawable.tree_palm,
            R.drawable.tree_papaya,
            R.drawable.tree_pine,
            R.drawable.tree_poplar,
            R.drawable.tree_rowan,
            R.drawable.tree_sakura,
            R.drawable.tree_sequoia,
            R.drawable.tree_tree,
            R.drawable.tree_wisteria
        )
    }

    private val tileIds by lazy {
        mutableListOf(
            R.drawable.tile_1,
            R.drawable.tile_2,
            R.drawable.tile_3,
            R.drawable.tile_4,
            R.drawable.tile_5,
            R.drawable.tile_6,
            R.drawable.tile_7,
            R.drawable.tile_8,
            R.drawable.tile_9,
            R.drawable.tile_10,
            R.drawable.tile_11,
            R.drawable.tile_12,
            R.drawable.tile_13,
            R.drawable.tile_14,
            R.drawable.tile_15,
            R.drawable.tile_16,
            R.drawable.tile_17,
            R.drawable.tile_18,
            R.drawable.tile_19,
            R.drawable.tile_20,
            R.drawable.tile_21,
            R.drawable.tile_22,
            R.drawable.tile_23,
            R.drawable.tile_24,
            R.drawable.tile_25,
            R.drawable.tile_26,
            R.drawable.tile_27
        )
    }

    fun generateCards(quantity: Int, categoryList: List<Int>): List<CardModel> {
        val questionList = mutableListOf<CardModel>()

        for (i in 1..(quantity / 2)) {
            val category = categoryList.shuffled().first()

            val idCreated = createCardId(category)

            questionList.add(CardModel(id = idCreated, type = category))
            questionList.add(CardModel(id = idCreated, type = category))
        }

        val cardsList = mutableListOf<CardModel>()
        questionList.chunked(questionList.size / 2).forEach {
            cardsList.addAll(it.shuffled())
        }

        return cardsList.shuffled()
    }

    /**
     * Returns a card id based on the type.
     */
    private fun createCardId(type: Int): Int {
        return when (getByValue(type)) {
            NUMBER -> getIdByTypeList(numberIds)
            ANIMAL -> getIdByTypeList(animalIds)
            FLAG -> getIdByTypeList(flagIds)
            FOOD -> getIdByTypeList(foodIds)
            TREE -> getIdByTypeList(treeIds)
            else -> getIdByTypeList(tileIds)
        }
    }

    private fun getIdByTypeList(list: MutableList<Int>): Int {
        val value = list.shuffled().first()

        list.remove(value)

        return value
    }
}
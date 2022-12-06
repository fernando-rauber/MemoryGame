package uk.fernando.memory.datastore

interface GamePrefsStore {

    suspend fun getBoardSize(): Int
    suspend fun getCategoryList(): List<Int>

    suspend fun storeBoardSize(value: Int)
    suspend fun storeCategory(value: List<Int>)
}
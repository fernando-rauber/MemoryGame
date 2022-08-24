package uk.fernando.memory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.fernando.memory.database.dao.LevelDao
import uk.fernando.memory.database.dao.CategoryDao
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.CategoryEntity

@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [CategoryEntity::class, LevelEntity::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun categoryDAO(): CategoryDao
    abstract fun levelDAO(): LevelDao
}

const val DATABASE_VERSION = 1

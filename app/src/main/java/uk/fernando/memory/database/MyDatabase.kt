package uk.fernando.memory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.fernando.memory.database.converter.DateTypeConverter
import uk.fernando.memory.database.converter.ListTypeConverter
import uk.fernando.memory.database.dao.LevelDao
import uk.fernando.memory.database.dao.CategoryDao
import uk.fernando.memory.database.dao.ScoreDao
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.CategoryEntity
import uk.fernando.memory.database.entity.ScoreEntity

@TypeConverters(DateTypeConverter::class, ListTypeConverter::class)
@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [CategoryEntity::class, LevelEntity::class, ScoreEntity::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun categoryDAO(): CategoryDao
    abstract fun levelDAO(): LevelDao
    abstract fun scoreDAO(): ScoreDao
}

const val DATABASE_VERSION = 2

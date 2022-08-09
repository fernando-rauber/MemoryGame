package uk.fernando.memory.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.fernando.memory.database.dao.LevelDao
import uk.fernando.memory.database.dao.MapDao
import uk.fernando.memory.database.entity.LevelEntity
import uk.fernando.memory.database.entity.MapEntity

@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [MapEntity::class, LevelEntity::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun mapDAO(): MapDao
    abstract fun levelDAO(): LevelDao
}

const val DATABASE_VERSION = 1

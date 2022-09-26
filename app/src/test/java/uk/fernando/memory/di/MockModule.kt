package uk.fernando.memory.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import io.mockk.mockk
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import uk.fernando.memory.database.MyDatabase
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.datastore.PrefsStoreMock
import uk.fernando.memory.di.KoinModule.allModules
import uk.fernando.memory.repository.CategoryRepository
import uk.fernando.memory.repository.CategoryRepositoryMock
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.repository.LevelRepositoryMock

val mockModule = module {
    single { mockk<Application>() }
    single { mockk<Context>() }
    single<PrefsStore> { PrefsStoreMock() }
    single { Room.inMemoryDatabaseBuilder(get(), MyDatabase::class.java) }
    single { KoinModule.getAndroidLogger() }
    factory(qualifier = StringQualifier("common")) { mockk<SharedPreferences>() }
}

val mockedDAOModule = module {
    single<CategoryRepository> { CategoryRepositoryMock() }
    single<LevelRepository> { LevelRepositoryMock() }
}

fun allMockedModules() = allModules() + mockModule + mockedDAOModule
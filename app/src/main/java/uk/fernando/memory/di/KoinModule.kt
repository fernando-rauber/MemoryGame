package uk.fernando.memory.di


import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.fernando.logger.AndroidLogger
import uk.fernando.logger.MyLogger
import uk.fernando.memory.BuildConfig
import uk.fernando.memory.database.MyDatabase
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.datastore.PrefsStoreImpl

object KoinModule {

    /**
     * Keep the order applied
     * @return List<Module>
     */
    fun allModules(): List<Module> = listOf(coreModule, databaseModule, repositoryModule, useCaseModule, viewModelModule)

    private val coreModule = module {

        single { getAndroidLogger() }
        single<PrefsStore> { PrefsStoreImpl(androidApplication()) }
    }

    private val databaseModule = module {

        fun provideDatabase(application: Application): MyDatabase {
            return Room.databaseBuilder(application, MyDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single { provideDatabase(androidApplication()) }
        factory { get<MyDatabase>().mapDAO() }
        factory { get<MyDatabase>().levelDAO() }
    }

    private val repositoryModule: Module
        get() = module {
//            factory<TimeRepository> { TimeRepositoryImpl(get()) }
        }

    private val useCaseModule: Module
        get() = module {
//            single { InsertTimeUseCase(get(), get()) }
//            single { GetTimeListUseCase(get()) }
        }

    private val viewModelModule: Module
        get() = module {

//            viewModel { SplashViewModel(get()) }
//            viewModel { SettingsViewModel(get()) }
        }

    private const val DB_NAME = "memory_game.db"

    fun getAndroidLogger(): MyLogger {
        return if (BuildConfig.BUILD_TYPE == "debug")
            AndroidLogger(MyLogger.LogLevel.DEBUG)
        else
            AndroidLogger(MyLogger.LogLevel.ERROR)
    }
}



package uk.fernando.memory.di


import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.fernando.logger.AndroidLogger
import uk.fernando.logger.MyLogger
import uk.fernando.memory.BuildConfig
import uk.fernando.memory.database.MyDatabase
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.datastore.PrefsStoreImpl
import uk.fernando.memory.repository.CategoryRepository
import uk.fernando.memory.repository.CategoryRepositoryImpl
import uk.fernando.memory.repository.LevelRepository
import uk.fernando.memory.repository.LevelRepositoryImpl
import uk.fernando.memory.usecase.*
import uk.fernando.memory.viewmodel.GameViewModel
import uk.fernando.memory.viewmodel.HomeViewModel
import uk.fernando.memory.viewmodel.SettingsViewModel
import uk.fernando.memory.viewmodel.SplashViewModel

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
        factory { get<MyDatabase>().categoryDAO() }
        factory { get<MyDatabase>().levelDAO() }
    }

    private val repositoryModule: Module
        get() = module {
            factory<CategoryRepository> { CategoryRepositoryImpl(get()) }
            factory<LevelRepository> { LevelRepositoryImpl(get()) }
        }

    private val useCaseModule: Module
        get() = module {
            single { GetCategoryListUseCase(get()) }
            single { UpdateLevelUseCase(get(), get()) }
            single { PurchaseUseCase(androidApplication(), get(), get()) }
            single { SetUpUseCase(get(), get(), get()) }
        }

    private val viewModelModule: Module
        get() = module {

            viewModel { SplashViewModel(get(), get()) }
            viewModel { HomeViewModel(get()) }
            viewModel { SettingsViewModel(get(), get()) }
            viewModel { GameViewModel(get(), get(), get(), get()) }
        }

    private const val DB_NAME = "memory_game.db"

    fun getAndroidLogger(): MyLogger {
        return if (BuildConfig.BUILD_TYPE == "debug")
            AndroidLogger(MyLogger.LogLevel.DEBUG)
        else
            AndroidLogger(MyLogger.LogLevel.ERROR)
    }
}
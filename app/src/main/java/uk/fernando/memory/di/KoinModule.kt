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
import uk.fernando.memory.datastore.GamePrefsStore
import uk.fernando.memory.datastore.GamePrefsStoreImpl
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.datastore.PrefsStoreImpl
import uk.fernando.memory.repository.*
import uk.fernando.memory.usecase.*
import uk.fernando.memory.viewmodel.campaign.GameViewModel
import uk.fernando.memory.viewmodel.campaign.LevelViewModel
import uk.fernando.memory.viewmodel.SettingsViewModel
import uk.fernando.memory.viewmodel.SplashViewModel
import uk.fernando.memory.viewmodel.custom.CreateGameViewModel
import uk.fernando.memory.viewmodel.custom.ScoreViewModel

object KoinModule {

    /**
     * Keep the order applied
     * @return List<Module>
     */
    fun allModules(): List<Module> = listOf(coreModule, databaseModule, repositoryModule, useCaseModule, viewModelModule)

    private val coreModule = module {

        single { getAndroidLogger() }
        single<PrefsStore> { PrefsStoreImpl(androidApplication()) }
        single<GamePrefsStore> { GamePrefsStoreImpl(androidApplication()) }
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
        factory { get<MyDatabase>().scoreDAO() }
    }

    private val repositoryModule: Module
        get() = module {
            factory<CategoryRepository> { CategoryRepositoryImpl(get()) }
            factory<LevelRepository> { LevelRepositoryImpl(get()) }
            factory<ScoreRepository> { ScoreRepositoryImpl(get()) }
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
            viewModel { LevelViewModel(get()) }
            viewModel { SettingsViewModel(get(), get()) }
            viewModel { GameViewModel(get(), get(), get(), get()) }

            viewModel { CreateGameViewModel(get()) }
            viewModel { ScoreViewModel(get()) }
        }

    private const val DB_NAME = "memory_game.db"

    fun getAndroidLogger(): MyLogger {
        return if (BuildConfig.BUILD_TYPE == "debug")
            AndroidLogger(MyLogger.LogLevel.DEBUG)
        else
            AndroidLogger(MyLogger.LogLevel.ERROR)
    }
}
package uk.fernando.memory.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavigationCommand {
    val path: String
    val arguments: List<NamedNavArgument>

    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun withArgsFormat(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach { arg ->
                append("/{$arg}")
            }
        }
    }
}

object Directions {

    val splash = object : NavigationCommand {
        override val path: String
            get() = "splash"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val home = object : NavigationCommand {
        override val path: String
            get() = "home"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val level = object : NavigationCommand {
        override val path: String
            get() = "level"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val campaignGame = object : NavigationCommand {
        override val path: String
            get() = "campaign_game"
        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(LEVEL_ID) { type = NavType.StringType },
                navArgument(CATEGORY_ID) { type = NavType.StringType },
            )
    }

    val createGame = object : NavigationCommand {
        override val path: String
            get() = "create_game"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    val customGame = object : NavigationCommand {
        override val path: String
            get() = "custom_game"
        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(BOARD_SIZE) { type = NavType.StringType }
            )
    }

    val settings = object : NavigationCommand {
        override val path: String
            get() = "settings"
        override val arguments: List<NamedNavArgument>
            get() = emptyList()
    }

    const val LEVEL_ID = "levelId"
    const val CATEGORY_ID = "categoryId"
    const val BOARD_SIZE = "board_size"
}



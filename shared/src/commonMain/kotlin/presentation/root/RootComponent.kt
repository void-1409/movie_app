package presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.screens.home.HomeViewModel

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()
    private val homeViewModel: HomeViewModel by inject()

    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Home,
            handleBackButton = true,
            serializer = Config.serializer(),
            childFactory = ::createChild
        )

    private fun createChild(config: Config, context: ComponentContext): Child =
        when (config) {
            Config.Home -> Child.Home(homeViewModel)
            Config.Movies -> Child.Movies
            Config.Tickets -> Child.Tickets
            Config.Profile -> Child.Profile
            is Config.Detail -> Child.Detail(config.movieId)
        }

    fun onTabClick(tab: MainNavTab) {
        when (tab) {
            MainNavTab.HOME -> navigation.replaceAll(Config.Home)
            MainNavTab.MOVIES -> navigation.replaceAll(Config.Movies)
            MainNavTab.TICKETS -> navigation.replaceAll(Config.Tickets)
            MainNavTab.PROFILE -> navigation.replaceAll(Config.Profile)
        }
    }
    // when clicked on a movie card
    @OptIn(DelicateDecomposeApi::class)
    fun onMovieClick(movieId: Int) {
        navigation.push(Config.Detail(movieId = movieId))
    }
    fun onBackClicked() {
        navigation.pop()
    }

    sealed class Child {
        data class Home(val viewModel: HomeViewModel) : Child()
        data object Movies : Child()
        data object Tickets : Child()
        data object Profile : Child()
        data class Detail(val movieId: Int) : Child()
    }

    @Serializable // Use @Serializable on the parent sealed interface
    private sealed interface Config {
        @Serializable
        data object Home : Config
        @Serializable
        data object Movies : Config
        @Serializable
        data object Tickets : Config
        @Serializable
        data object Profile : Config
        @Serializable
        data class Detail(val movieId: Int) : Config
    }
}

enum class MainNavTab {
    HOME, MOVIES, TICKETS, PROFILE
}
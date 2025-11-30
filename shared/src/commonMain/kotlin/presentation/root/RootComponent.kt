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
import com.arkivanov.essenty.backhandler.BackCallback
import domain.model.Ticket
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import presentation.screens.detail.MovieDetailViewModel
import presentation.screens.home.HomeViewModel
import presentation.screens.movies.MovieTab
import presentation.screens.movies.MoviesViewModel
import presentation.screens.seats.SeatSelectionViewModel
import presentation.screens.shows.ShowsViewModel
import presentation.screens.tickets.TicketsViewModel
import domain.repository.TicketRepository
import org.koin.core.component.get

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()
    private val homeViewModel: HomeViewModel by inject()
    private val moviesViewModel: MoviesViewModel by inject()

    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Home,
            handleBackButton = true,
            serializer = Config.serializer(),
            childFactory = ::createChild
        )

    // to handle back press on tabs other than home tab
    private val backCallback = BackCallback {
        // get current active screen
        val activeChild = childStack.value.active.instance

        // check if on main tab but NOT on the Home screen
        if (
            activeChild !is Child.Home &&
            (activeChild is Child.Movies || activeChild is Child.Tickets || activeChild is Child.Profile)
        ) {
            // navigate back to home screen
            onTabClick(MainNavTab.HOME)
        } else {
            // otherwise (on home screen or sub-screens), default action
            navigation.pop()
        }
    }

    init {
        backHandler.register(backCallback)
    }

    private fun createChild(config: Config, context: ComponentContext): Child =
        when (config) {
            is Config.Detail -> {
                // use koin to create instance of movie detail screen viewmodel
                val viewModel: MovieDetailViewModel by inject { parametersOf(config.movieId) }
                Child.Detail(viewModel)
            }
            is Config.Shows -> {
                val viewModel: ShowsViewModel by inject { parametersOf(config.movieTitle) }
                Child.Shows(viewModel, config.movieId)
            }
            is Config.SeatSelection -> {
                val viewModel: SeatSelectionViewModel by inject{
                    parametersOf(config.movieId, config.cinema, LocalDate.parse(config.date), config.time)
                }
                Child.SeatSelection(
                    viewModel = viewModel,
                    movieId = config.movieId,
                    movieTitle = config.movieTitle
                )
            }
            is Config.Confirmation -> Child.Confirmation(config.ticketId)
            is Config.Tickets -> {
                val viewModel: TicketsViewModel by inject()
                Child.Tickets(viewModel)
            }
            Config.Home -> Child.Home(homeViewModel)
            Config.Movies -> Child.Movies(moviesViewModel)
            Config.Profile -> Child.Profile
            is Config.TicketDetail -> {
                val ticketRepository: TicketRepository = get()

                val ticket = ticketRepository.getTickets().find { it.id == config.ticketId }
                if (ticket != null) {
                    Child.TicketDetail(ticket)
                } else {
                    val viewModel: TicketsViewModel by inject()
                    Child.Tickets(viewModel)
                }
            }
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

    sealed class Child {
        data class Home(val viewModel: HomeViewModel) : Child()
        data class Movies(val viewModel: MoviesViewModel) : Child()
        data class Tickets(val viewModel: TicketsViewModel) : Child()
        data object Profile : Child()
        data class Detail(val viewModel: MovieDetailViewModel) : Child()
        data class Shows(val viewModel: ShowsViewModel, val movieId: Int) : Child()
        data class SeatSelection(
            val viewModel: SeatSelectionViewModel,
            val movieId: Int,
            val movieTitle: String
        ) : Child()
        data class Confirmation(val ticketId: String) : Child()
        data class TicketDetail(val ticket: Ticket) : Child()
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
        @Serializable
        data class Shows(val movieId: Int, val movieTitle: String) : Config
        @Serializable
        data class SeatSelection(
            val movieId: Int,
            val movieTitle: String,
            val cinema: String,
            val date: String,
            val time: String
        ) : Config
        @Serializable
        data class Confirmation(val ticketId: String) : Config
        @Serializable
        data class TicketDetail(val ticketId: String) : Config
    }

    fun onBackClicked() {
        navigation.pop()
    }

    // function for handling navigation from HomeScreen arrows
    fun onNavigateToMovies(initialTab: MovieTab) {
        moviesViewModel.onTabSelected(initialTab)   // set tab in viewmodel
        navigation.replaceAll(Config.Movies)        // navigate to movies screen
    }

    @OptIn(DelicateDecomposeApi::class)
    fun onNavigateToShows(movieId: Int, movieTitle: String) {
        navigation.push(Config.Shows(movieId, movieTitle))
    }

    @OptIn(DelicateDecomposeApi::class)
    fun onNavigateToSeatSelection(movieId: Int, movieTitle: String, cinema: String, date: LocalDate, time: String) {
        navigation.push(Config.SeatSelection(movieId, movieTitle, cinema, date.toString(), time))
    }

    fun onNavigateToConfirmation(ticketId: String) {
        navigation.push(Config.Confirmation(ticketId = ticketId))
    }

    fun onNavigateToTickets() {
        navigation.replaceAll(Config.Tickets)
    }

    fun onNavigateToTicketDetail(ticketId: String) {
        navigation.push(Config.TicketDetail(ticketId))
    }
}

enum class MainNavTab {
    HOME, MOVIES, TICKETS, PROFILE
}
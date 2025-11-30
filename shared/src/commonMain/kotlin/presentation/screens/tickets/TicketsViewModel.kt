package presentation.screens.tickets

import androidx.lifecycle.ViewModel
import domain.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TicketsViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsState())
    val uiState = _uiState.asStateFlow()

    // call this whenever screen becomes visible to ensure updated data
    fun loadTickets() {
        val currentTickets = repository.getTickets()
        _uiState.update { it.copy(tickets = currentTickets) }
    }
}
package presentation.screens.tickets

import domain.model.Ticket

data class TicketsState(
    val tickets: List<Ticket> = emptyList()
)
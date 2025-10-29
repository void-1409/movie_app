package domain.repository

import domain.model.Ticket

interface TicketRepository {
    fun addTicket(ticket: Ticket)
    fun getTickets(): List<Ticket>
}

// simple in-memory implementation
object TicketRepositoryImpl : TicketRepository {
    private val tickets = mutableListOf<Ticket>()

    override fun addTicket(ticket: Ticket) {
        tickets.add(0, ticket)
    }

    override fun getTickets(): List<Ticket> {
        return tickets
    }
}
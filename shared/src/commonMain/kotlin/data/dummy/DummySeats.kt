package data.dummy

import domain.model.Seat
import domain.model.SeatStatus
import kotlin.random.Random

/*
* Generates a dummy list of seats for cinema hall
* returns a list of seat objects
*/

fun generateDummySeatLayout(rows: Int = 8, seatsPerRow: Int = 10): List<Seat> {
    val seats = mutableListOf<Seat>()
    val rowChars = ('A'..'Z').toList()

    (0 until rows).forEach { rowIndex ->
        val rowChar = rowChars[rowIndex]
        (1..seatsPerRow).forEach { seatNum ->
            val status = if (Random.nextFloat() < 0.25) {
                SeatStatus.RESERVED
            } else {
                SeatStatus.AVAILABLE
            }
            seats.add(Seat(row = rowChar, number = seatNum, status = status))
        }
    }
    return seats
}
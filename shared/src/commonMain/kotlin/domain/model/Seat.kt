package domain.model

enum class SeatStatus {
    AVAILABLE,
    RESERVED,
    SELECTED
}

data class Seat (
    val row: Char,
    val number: Int,
    var status: SeatStatus
) {
    // unique identifier for each seat, "A1" "C11"
    val id: String = "$row$number"
}
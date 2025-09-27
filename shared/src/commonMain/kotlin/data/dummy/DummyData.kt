package data.dummy

import domain.model.CinemaShowtimes

val dummyShowtimes = listOf(
    CinemaShowtimes(
        cinemaName = "Lichtspielhaus Deggendorf",
        showtimes = listOf("10:30", "13:00", "14:30", "18:00", "19:30")
    ),
    CinemaShowtimes(
        cinemaName = "CineMax MediaMarkt",
        showtimes = listOf("17:30", "18:00", "21:00", "22:30")
    ),CinemaShowtimes(
        cinemaName = "Sanelite Cinema",
        showtimes = listOf("13:00", "15:00", "21:30")
    )
)
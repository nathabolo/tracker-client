package com.example.tracker_client

sealed class ApiEndPoint {
    sealed class Tracker {
        data class ArrivalTime(val lineId: String) : Tracker()
        data class RouteStops(val lineId: String) : Tracker()
        data class Search(val query: String) : Tracker()
        data class Journey(val from: String, val to: String) : Tracker()

        val path: String
            get() = when (this) {
                is ArrivalTime -> "/Line/$lineId/Arrivals"
                is RouteStops -> "/Line/$lineId/Route/Sequence/outbound"
                is Search -> "/StopPoint/Search/$query?modes=bus"
                is Journey -> "/Journey/JourneyResults/$from/to/$to"
            }
    }
}
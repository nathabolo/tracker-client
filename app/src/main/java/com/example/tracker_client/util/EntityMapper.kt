package com.example.tracker_client.util

import com.example.tracker_client.dto.ArrivalTimeResponse
import com.example.tracker_client.dto.BusStopCoordinatesResponse
import com.example.tracker_domain.entity.ArrivalTime
import com.example.tracker_domain.entity.BusStopCoordinates

fun ArrivalTimeResponse.toDomain(): ArrivalTime {
    return ArrivalTime(
        vehicleId = this.vehicleId,
        accessNodeId = this.accessNodeId,
        timeArrivedToStation = this.timeArrivedToStation
    )
}

fun BusStopCoordinatesResponse.toDomain(): BusStopCoordinates {
    return BusStopCoordinates(
        vehicleId = this.vehicleId,
        latitude = this.latitude,
        longitude = this.longitude,
        movementTime = this.movementTime
    )
}
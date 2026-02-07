package com.example.tracker_client.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BusStopCoordinatesResponse(
    @SerializedName("vehicleId") val vehicleId: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("movementTime") val movementTime: Int
)
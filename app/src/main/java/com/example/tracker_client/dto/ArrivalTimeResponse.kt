package com.example.tracker_client.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ArrivalTimeResponse(
    @SerializedName("vehicleId") val vehicleId: String,
    @SerializedName("accessNodeId") val accessNodeId: String,
    @SerializedName("timeArrivedToStation") val timeArrivedToStation: Int
)
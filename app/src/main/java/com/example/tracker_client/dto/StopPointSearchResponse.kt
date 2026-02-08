package com.example.tracker_client.dto

import com.google.gson.annotations.SerializedName

data class StopPointSearchResponse(
    @SerializedName("matches")
    val matches: List<StopPointDto>
)

data class StopPointDto(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double
)

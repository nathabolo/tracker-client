package com.example.tracker_client.dto

import com.example.tracker_domain.entity.StopPoint

fun StopPointDto.toDomain(): StopPoint =
    StopPoint(
        id = id,
        name = name,
        lat = lat,
        lon = lon
    )

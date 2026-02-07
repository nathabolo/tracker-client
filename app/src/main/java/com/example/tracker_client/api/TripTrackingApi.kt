package com.example.tracker_client.api

import com.example.tracker_client.ApiEndPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface ITripTrackingApi {
    suspend fun getArrivalTime(arrivalId: String): Response
    suspend fun getBusRouteStops(arrivalId: String): Response
    suspend fun getJourney(from: String, to: String): Response
    suspend fun searchStopPoints(query: String): Response
}

class TripTrackingApi(
    private val client: OkHttpClient,
    private val baseUrl: String
) : ITripTrackingApi {

    override suspend fun getArrivalTime(arrivalId: String): Response {
        val request = Request.Builder()
            .url(baseUrl + ApiEndPoint.Tracker.ArrivalTime(arrivalId).path)
            .get()
            .build()
        return client.newCall(request).execute()
    }

    override suspend fun getBusRouteStops(arrivalId: String): Response {
        val request = Request.Builder()
            .url(baseUrl + ApiEndPoint.Tracker.RouteStops(arrivalId).path)
            .get()
            .build()
        return client.newCall(request).execute()
    }

    override suspend fun getJourney(from: String, to: String): Response {
        val request = Request.Builder()
            .url(baseUrl + ApiEndPoint.Tracker.Journey(from, to).path)
            .get()
            .build()
        return client.newCall(request).execute()
    }

    override suspend fun searchStopPoints(query: String): Response {
        val request = Request.Builder()
            .url(baseUrl + ApiEndPoint.Tracker.Search(query).path)
            .get()
            .build()
        return client.newCall(request).execute()
    }
}
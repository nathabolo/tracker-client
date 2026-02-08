package com.example.tracker_client.repository

import com.example.tracker_client.api.ITripTrackingApi
import com.example.tracker_client.dto.StopPointSearchResponse
import com.example.tracker_client.dto.toDomain
import com.example.tracker_domain.entity.ArrivalTime
import com.example.tracker_domain.entity.BusStopCoordinates
import com.example.tracker_domain.entity.RouteJouney
import com.example.tracker_domain.entity.StopPoint
import com.example.tracker_domain.repository.ITripTrackingRepository
import com.google.gson.Gson

class TrackerClientRepositoryImpl(
    private val trackerApi: ITripTrackingApi,
    private val gson: Gson
) : ITripTrackingRepository {

    private val recentSearches = LinkedHashSet<String>()

    override suspend fun getArrivalTime(arrivalId: String): Result<ArrivalTime> {
        return try {
            val response = trackerApi.getArrivalTime(arrivalId)
            if (!response.isSuccessful) {
                Result.failure(Exception("Api Error: ${response.code} + ${response.message}"))
            } else {
                val bodyString = response.body?.string()
                val data = gson.fromJson(bodyString, ArrivalTime::class.java)
                data?.toEntity()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid Response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBusRouteStops(arrivalId: String): Result<BusStopCoordinates> {
        return try {
            val response = trackerApi.getBusRouteStops(arrivalId)
            if (!response.isSuccessful) {
                Result.failure(Exception("Api Error: ${response.code} + ${response.message}"))
            } else {
                val bodyString = response.body?.string()
                val data = gson.fromJson(bodyString, BusStopCoordinates::class.java)
                data?.toEntity()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid Response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getRecentSearches(): List<String> =
        recentSearches.toList().reversed()
    override suspend fun searchStopPoints(query: String): Result<List<StopPoint>> {
        return try {
            val response = trackerApi.searchStopPoints(query)
            if (!response.isSuccessful) {
                Result.failure(Exception("API error: ${response.code}"))
            } else {
                val bodyString = response.body?.string()
                    ?: return Result.failure(Exception("Empty response"))

                val searchResponse =
                    gson.fromJson(bodyString, StopPointSearchResponse::class.java)
                val results: List<StopPoint> =
                    searchResponse.matches.map { it.toDomain() }

                recentSearches.remove(query)
                recentSearches.add(query)

                Result.success(results)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJourney(from: String, to: String): Result<RouteJouney> {
        return try {
            val response = trackerApi.getJourney(from, to)
            if (!response.isSuccessful) {
                Result.failure(Exception("Api Error: ${response.code} + ${response.message}"))
            } else {
                val body = response.body ?: return Result.failure(Exception("Invalid Response"))
                val bodyString = body.string()
                val data = gson.fromJson(bodyString, RouteJouney::class.java)

                data?.toEntity()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid Response"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
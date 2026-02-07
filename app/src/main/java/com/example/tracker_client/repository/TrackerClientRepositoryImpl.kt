package com.example.tracker_client.repository

import com.example.tracker_client.api.ITripTrackingApi
import com.example.tracker_domain.entity.ArrivalTime
import com.example.tracker_domain.entity.BusStopCoordinates
import com.example.tracker_domain.entity.RouteJouney
import com.example.tracker_domain.entity.StopPoint
import com.example.tracker_domain.repository.ITripTrackingRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackerClientRepositoryImpl(
    private val trackerApi: ITripTrackingApi,
    private val gson: Gson
) : ITripTrackingRepository {

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

    override suspend fun searchStopPoints(query: String): Result<List<StopPoint>> {
        return try {
            val response = trackerApi.searchStopPoints(query)
            if (!response.isSuccessful) {
                Result.failure(Exception("Api Error: ${response.code} + ${response.message}"))
            } else {
                val bodyString = response.body?.string()
                val type = object : TypeToken<List<StopPoint>>() {}.type
                val data = gson.fromJson<List<StopPoint>>(bodyString, type)
                data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid Response"))
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
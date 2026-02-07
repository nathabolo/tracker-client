package com.example.tracker_client.repository

import com.example.tracker_client.api.ITripTrackingApi
import com.example.tracker_domain.entity.ArrivalTime
import com.example.tracker_domain.entity.BusStopCoordinates
import com.example.tracker_domain.entity.RouteJouney
import com.example.tracker_domain.entity.StopPoint
import com.example.tracker_domain.repository.ITripTrackingRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class TrackerClientRepositoryTest {

    private val mockApi = mockk<ITripTrackingApi>()
    private val mockGson = mockk<Gson>()
    private lateinit var repository: ITripTrackingRepository

    @Before
    fun setUp() {
        repository = TrackerClientRepositoryImpl(mockApi, mockGson)
    }

    @Test
    fun `getArrivalTime invokes api and returns success`() {
        val arrivalId = "123"
        val mockBody = mockk<ArrivalTime>()
        val mockEntity = mockk<ArrivalTime>()

        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "{}"

        coEvery { mockApi.getArrivalTime(arrivalId) } returns mockResponse
        every { mockGson.fromJson(any<String>(), ArrivalTime::class.java) } returns mockBody
        every { mockBody.toEntity() } returns mockEntity

        val result = runBlocking { repository.getArrivalTime(arrivalId) }

        coVerify(exactly = 1) { mockApi.getArrivalTime(arrivalId) }
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(mockEntity, result.getOrNull())
    }

    @Test
    fun `getArrivalTime returns failure when api returns error`() {
        val arrivalId = "123"
        val mockResponse = mockk<Response>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns HttpURLConnection.HTTP_INTERNAL_ERROR
        every { mockResponse.message } returns "Internal Server Error"
        coEvery { mockApi.getArrivalTime(arrivalId) } returns mockResponse
        val result = runBlocking { repository.getArrivalTime(arrivalId) }
        Assert.assertTrue(result.isFailure)
        Assert.assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `getBusRouteStops returns success when api returns valid data`() {
        val arrivalId = "route1"
        val mockBody = mockk<BusStopCoordinates>()
        val mockEntity = mockk<BusStopCoordinates>()
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "{}"

        coEvery { mockApi.getBusRouteStops(arrivalId) } returns mockResponse
        every { mockGson.fromJson(any<String>(), BusStopCoordinates::class.java) } returns mockBody
        every { mockBody.toEntity() } returns mockEntity

        val result = runBlocking { repository.getBusRouteStops(arrivalId) }
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(mockEntity, result.getOrNull())
    }

    @Test
    fun `searchStopPoints returns success with list of points`() {
        val query = "Victoria"
        val mockList = listOf(mockk<StopPoint>())
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "[]"

        coEvery { mockApi.searchStopPoints(query) } returns mockResponse
        every {
            mockGson.fromJson<List<StopPoint>>(any<String>(), any<java.lang.reflect.Type>())
        } returns mockList

        val result = runBlocking { repository.searchStopPoints(query) }
        Assert.assertTrue("Expected success but got failure: ${result.exceptionOrNull()}", result.isSuccess)
        Assert.assertEquals(mockList, result.getOrNull())
    }

    @Test
    fun `getJourney returns failure when json is invalid`() {
        val from = "A"
        val to = "B"
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "invalid"

        coEvery { mockApi.getJourney(from, to) } returns mockResponse
        every { mockGson.fromJson(any<String>(), RouteJouney::class.java) } throws JsonSyntaxException("error")

        val result = runBlocking { repository.getJourney(from, to) }

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is JsonSyntaxException)
    }

    @Test
    fun `getJourney returns failure when response body is null`() {
        val from = "A"
        val to = "B"
        val mockResponse = mockk<Response>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns null

        coEvery { mockApi.getJourney(from, to) } returns mockResponse

        val result = runBlocking { repository.getJourney(from, to) }

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals("Invalid Response", result.exceptionOrNull()?.message)
    }
}
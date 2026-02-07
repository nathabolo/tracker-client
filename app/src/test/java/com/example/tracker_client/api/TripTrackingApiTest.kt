package com.example.tracker_client.api

import com.example.tracker_client.ApiEndPoint
import io.mockk.*
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.Call
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TripTrackingApiTest {

    private val mockClient = mockk<OkHttpClient>()
    private val mockCall = mockk<Call>()
    private val baseUrl = "https://api.tfl.gov.uk/"

    private lateinit var tripTrackingApi: ITripTrackingApi

    @Before
    fun setUp() {
        tripTrackingApi = TripTrackingApi(mockClient, baseUrl)
        every { mockClient.newCall(any()) } returns mockCall
    }

    @Test
    fun `getArrivalTime invokes client with correct url`() {
        val arrivalId = "12345"
        val requestCaptor = slot<Request>()
        val mockResponse = mockk<Response>()

        coEvery { mockCall.execute() } returns mockResponse

        runBlocking { tripTrackingApi.getArrivalTime(arrivalId) }

        verify(exactly = 1) { mockClient.newCall(capture(requestCaptor)) }

        val expectedUrl = baseUrl + ApiEndPoint.Tracker.ArrivalTime(arrivalId).path
        Assert.assertEquals(expectedUrl, requestCaptor.captured.url.toString())
        Assert.assertEquals("GET", requestCaptor.captured.method)
    }

    @Test
    fun `getBusRouteStops invokes client with correct url`() {
        val routeId = "9876"
        val requestCaptor = slot<Request>()
        val mockResponse = mockk<Response>()

        coEvery { mockCall.execute() } returns mockResponse

        runBlocking { tripTrackingApi.getBusRouteStops(routeId) }

        verify(exactly = 1) { mockClient.newCall(capture(requestCaptor)) }

        val expectedUrl = baseUrl + ApiEndPoint.Tracker.RouteStops(routeId).path
        Assert.assertEquals(expectedUrl, requestCaptor.captured.url.toString())
    }

    @Test
    fun `getJourney invokes client with correct url`() {
        val from = "PointA"
        val to = "PointB"
        val requestCaptor = slot<Request>()
        val mockResponse = mockk<Response>()

        coEvery { mockCall.execute() } returns mockResponse

        runBlocking { tripTrackingApi.getJourney(from, to) }

        verify(exactly = 1) { mockClient.newCall(capture(requestCaptor)) }

        val expectedUrl = baseUrl + ApiEndPoint.Tracker.Journey(from, to).path
        Assert.assertEquals(expectedUrl, requestCaptor.captured.url.toString())
    }

    @Test
    fun `searchStopPoints invokes client with correct url`() {
        val query = "Victoria"
        val requestCaptor = slot<Request>()
        val mockResponse = mockk<Response>()

        coEvery { mockCall.execute() } returns mockResponse

        runBlocking { tripTrackingApi.searchStopPoints(query) }

        verify(exactly = 1) { mockClient.newCall(capture(requestCaptor)) }

        val expectedUrl = baseUrl + ApiEndPoint.Tracker.Search(query).path
        Assert.assertEquals(expectedUrl, requestCaptor.captured.url.toString())
    }

    @Test
    fun `api returns response when service call is successful`() {
        val mockResponse = mockk<Response>()
        every { mockResponse.code } returns 200
        coEvery { mockCall.execute() } returns mockResponse

        val response = runBlocking { tripTrackingApi.searchStopPoints("test") }

        Assert.assertEquals(200, response.code)
    }
}
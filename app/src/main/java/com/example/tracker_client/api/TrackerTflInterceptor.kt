package com.example.tracker_client.api

import okhttp3.Interceptor
import okhttp3.Response

class TrackerTflInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlWithKey = originalUrl.newBuilder()
            .addQueryParameter("app_key", apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(urlWithKey)
            .build()

        return chain.proceed(newRequest)
    }
}
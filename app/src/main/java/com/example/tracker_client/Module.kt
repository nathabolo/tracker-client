package com.example.tracker_client

import com.example.tracker_client.api.ITripTrackingApi
import com.example.tracker_client.api.TrackerTflInterceptor
import com.example.tracker_client.api.TripTrackingApi
import com.example.tracker_client.repository.TrackerClientRepositoryImpl
import com.example.tracker_domain.repository.ITripTrackingRepository
import com.google.gson.Gson

import okhttp3.OkHttpClient
import org.koin.dsl.module

val trackerClientModule = module {
    single { Gson() }
    single { TrackerTflInterceptor(BuildConfig.TFL_API_KEY) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<TrackerTflInterceptor>())
            .build()
    }

    single<ITripTrackingApi> {
        TripTrackingApi(get(),
            "https://api.tfl.gov.uk/")
    }

    single<ITripTrackingRepository> { TrackerClientRepositoryImpl(get(), get()) }
}
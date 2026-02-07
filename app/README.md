# Android Tracker Client Module

## Importing the Tracker Client Module

In order to use the device domain within your project, you will need to register it's dependencies
with Koin, which can be done in one of the two ways.

Option One: Use the default implementations
```kt
startKoin {
    androidContext(this@ExampleApp)
    modules(
        // ...
        deviceDomainModule
    )
}
```

Option Two: Declare your own implementations for the dependencies
```kt
startKoin {
    androidContext(this@ExampleApp)
    modules(
        // ...
        module {
            factory { GetBusRouteStopsUseCase(get()) }
            factory { GetArrivalTimeUseCase(get()) }
            factory { GetJourneyResultsUseCase(get()) }
            factory { SearchLocationUseCase(get()) }
        }
    )
}
```
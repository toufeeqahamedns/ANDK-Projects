package com.udacity.asteroidradar

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "DEMO_API"
}

enum class ApiStatus {
    LOADING,
    DONE,
    ERROR
}

enum class AsteroidFilter {
    ALL,
    WEEKLY,
    TODAY
}
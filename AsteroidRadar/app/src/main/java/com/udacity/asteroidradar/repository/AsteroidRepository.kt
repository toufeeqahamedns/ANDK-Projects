package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.util.*

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    val weeklyAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            asteroidDatabase.asteroidDao.getWeeklyAsteroids(
                LocalDate.now().toString(), LocalDate.now().plusDays(7).toString()
            )
        ) {
            it?.asAsteroidModel()
        }

    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getAllAsteroids()) {
            it?.asAsteroidModel()
        }

    val todaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            asteroidDatabase.asteroidDao.getTodaysAsteroids(
                LocalDate.now().toString()
            )
        ) {
            it?.asAsteroidModel()
        }

    val dailyPhoto: LiveData<PictureOfDay> =
        Transformations.map(asteroidDatabase.asteroidDao.getDailyPhoto()) {
            it?.asPhotoModel()
        }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonResult = Network.asteroidsService.getAsteroidList(Constants.API_KEY)
            val asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult)).map {
                DatabaseAsteroid(
                    id = it.id,
                    codename = it.codename,
                    closeApproachDate = it.closeApproachDate,
                    absoluteMagnitude = it.absoluteMagnitude,
                    estimatedDiameter = it.estimatedDiameter,
                    relativeVelocity = it.relativeVelocity,
                    distanceFromEarth = it.distanceFromEarth,
                    isPotentiallyHazardous = it.isPotentiallyHazardous
                )
            }.toList()

            asteroidDatabase.asteroidDao.insertAll(asteroids)
        }
    }

    suspend fun refreshDailyPhoto() {
        withContext(Dispatchers.IO) {
            val jsonResult = Network.asteroidsService.getDailyPhoto(Constants.API_KEY)
            JSONObject(jsonResult).let {
                if (it.getString("media_type") == "image") {
                    asteroidDatabase.asteroidDao.insertPhoto(
                        DatabasePhoto(
                            date = it.getString("date"),
                            title = it.getString("title"),
                            url = it.getString("url")
                        )
                    )
                }
            }
        }
    }
}
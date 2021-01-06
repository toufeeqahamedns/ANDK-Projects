package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) {
            it?.asAsteroidModel()
        }

    val dailyPhoto: LiveData<PictureOfDay> =
        Transformations.map(asteroidDatabase.asteroidDao.getDailyPhoto()) {
            it?.asPhotoModel()
        }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonResult = Network.asteroidsService.getAsteroidList()
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
            val jsonResult = Network.asteroidsService.getDailyPhoto()
            JSONObject(jsonResult).let {
                if (it.getString("media_type") == "image") {
                    asteroidDatabase.asteroidDao.insertPhoto(
                        DatabasePhoto(
                            title = it.getString("title"),
                            url = it.getString("url")
                        )
                    )
                }
            }
        }
    }
}
package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "asteroids")
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
)

fun List<DatabaseAsteroid>.asAsteroidModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            absoluteMagnitude = it.absoluteMagnitude,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
        )
    }
}

@Entity(tableName = "dailyphoto")
data class DatabasePhoto(
    @PrimaryKey
    val date: String,
    val url: String,
    val title: String,
)

fun DatabasePhoto.asPhotoModel(): PictureOfDay {
    return PictureOfDay(title, url)
}

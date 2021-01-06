package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

private lateinit var INSTANCE: AsteroidDatabase

@Dao
interface AsteroidDao {

    @Query("select * from asteroids where closeApproachDate between :startDate and :endDate order by closeApproachDate asc")
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from asteroids order by closeApproachDate asc")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from asteroids where closeApproachDate = :today")
    fun getTodaysAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<DatabaseAsteroid>)

    @Query("select * from dailyphoto order by date desc")
    fun getDailyPhoto(): LiveData<DatabasePhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: DatabasePhoto)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePhoto::class], version = 7)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).fallbackToDestructiveMigration().build()
        }
        return INSTANCE
    }
}

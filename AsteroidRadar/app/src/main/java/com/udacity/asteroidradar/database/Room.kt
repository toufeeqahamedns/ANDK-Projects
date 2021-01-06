package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

private lateinit var INSTANCE: AsteroidDatabase

@Dao
interface AsteroidDao {
    @Query("select * from asteroids")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroid: List<DatabaseAsteroid>)

    @Query("select * from dailyphoto limit 1 ")
    fun getDailyPhoto(): LiveData<DatabasePhoto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: DatabasePhoto)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePhoto::class], version = 2)
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

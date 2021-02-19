package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext = appContext, params = params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val dataBase = getDatabase(applicationContext)
        val repository = AsteroidRepository(dataBase)

        return try {
            repository.refreshAsteroids()
            repository.refreshDailyPhoto()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.ApiStatus
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidFilter
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid>
        get() = _navigateToDetail

    private val _asteroidFilter = MutableLiveData<AsteroidFilter>(AsteroidFilter.WEEKLY)
    val asteroidFilter: LiveData<AsteroidFilter>
        get() = _asteroidFilter

    init {
        _apiStatus.value = ApiStatus.LOADING
        refreshAsteroids()
    }

    val asteroid = Transformations.switchMap(_asteroidFilter) {
        when (it!!) {
            AsteroidFilter.WEEKLY -> repository.weeklyAsteroids
            AsteroidFilter.ALL -> repository.allAsteroids
            else -> repository.todaysAsteroids
        }
    }

    val pictureOfDay = repository.dailyPhoto

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                _apiStatus.value = ApiStatus.LOADING
                repository.refreshAsteroids()
                repository.refreshDailyPhoto()
                _apiStatus.value = ApiStatus.DONE
            } catch (e: Exception) {
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onNavigatedToDetail() {
        _navigateToDetail.value = null
    }

    fun setAsteroidFilter(asteroidFilter: AsteroidFilter) {
        _asteroidFilter.value = asteroidFilter
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
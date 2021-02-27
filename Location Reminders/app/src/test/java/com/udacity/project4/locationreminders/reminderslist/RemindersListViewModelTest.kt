package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    // TODO: provide testing to the RemindersListViewModel and its live data objects
    private val dataSource = FakeDataSource()
    private lateinit var viewModel: RemindersListViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() = runBlocking {
        val reminder1 = ReminderDTO("Title1", "Banglore", "Banglore", 12.9716, 77.5946)
        val reminder2 = ReminderDTO("Title2", "Molkalmuru", "Molkalmuru", 14.7165, 76.7466)
        dataSource.saveReminder(reminder1)
        dataSource.saveReminder(reminder2)

        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun loadingReminders_shouldShowLoading() {
        mainCoroutineRule.pauseDispatcher()

        viewModel.loadReminders()

        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadingReminders_shouldShowError() {
        dataSource.shouldReturnError = true
        viewModel.loadReminders()
        assertThat(viewModel.showSnackBar.getOrAwaitValue(), notNullValue())
    }

    @Test
    fun clearData_noReminders_showNoDataTrue() = runBlocking {
        dataSource.deleteAllReminders()
        viewModel.loadReminders()
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
    }
}
package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import junit.framework.Assert

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    //TODO: provide testing to the SaveReminderView and its live data objects
    private val dataSource: ReminderDataSource = FakeDataSource()
    private lateinit var viewModel: SaveReminderViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() = runBlocking {
        val reminder1 = ReminderDTO("Title1", "Banglore", "Banglore", 12.9716, 77.5946)
        val reminder2 = ReminderDTO("Title2", "Molkalmuru", "Molkalmuru", 14.7165, 76.7466)
        dataSource.saveReminder(reminder1)
        dataSource.saveReminder(reminder2)

        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun onViewModeClear_setAllToNull() {
        viewModel.onClear()
        assertThat(
            viewModel.reminderTitle.getOrAwaitValue(),
            nullValue()
        )
        assertThat(
            viewModel.reminderDescription.getOrAwaitValue(),
            nullValue()
        )
        assertThat(
            viewModel.reminderSelectedLocationStr.getOrAwaitValue(),
            nullValue()
        )
        assertThat(viewModel.selectedPOI.getOrAwaitValue(), nullValue())
        assertThat(viewModel.latitude.getOrAwaitValue(), nullValue())
        assertThat(viewModel.longitude.getOrAwaitValue(), nullValue())
    }

    @Test
    fun validateAndSave_nullTitle_setsSnackbarText(){
        val newReminder= ReminderDataItem(null,"","",0.0,0.0)
        viewModel.validateEnteredData(newReminder)
        val text = viewModel.showSnackBarInt.getOrAwaitValue()
        assertThat(text, CoreMatchers.`is`(R.string.err_enter_title))
    }

    @Test
    fun validateAndSave_nullLocation_setsSnackbarText(){
        val newReminder= ReminderDataItem("title","",null,0.0,0.0)
        viewModel.validateEnteredData(newReminder)
        val text = viewModel.showSnackBarInt.getOrAwaitValue()
        assertThat(text, CoreMatchers.`is`(R.string.err_select_location))
    }

    @Test
    fun saveReminder_showLoadingToast_navigateBack(){
        val newReminder= ReminderDataItem("title","description","location",0.0,0.0)
        mainCoroutineRule.pauseDispatcher()

        viewModel.saveReminder(newReminder)
        assertThat(viewModel.showLoading.getOrAwaitValue(), Is.`is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showLoading.getOrAwaitValue(), Is.`is`(false))
        assertThat(viewModel.showToast.getOrAwaitValue(), Is.`is`("Reminder Saved !"))
        Assert.assertEquals(viewModel.navigationCommand.getOrAwaitValue(), NavigationCommand.Back)

    }

}
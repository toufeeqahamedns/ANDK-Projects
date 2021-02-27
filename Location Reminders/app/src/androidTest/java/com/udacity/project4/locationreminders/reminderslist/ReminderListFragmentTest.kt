package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.base.DataBindingViewHolder
import com.udacity.project4.locationreminders.data.FakeAndroidTestDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.lang.Appendable

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    // TODO: test the navigation of the fragments.
    // TODO: test the displayed data on the UI.
    // TODO: add testing for the error messages.

    private lateinit var dataSource: ReminderDataSource
    private lateinit var appContext: Application

    val reminder1 = ReminderDTO("Title1", "Banglore Description", "Banglore", 12.9716, 77.5946)
    val reminder2 = ReminderDTO("Title2", "Molkalmuru Description", "Molkalmuru", 14.7165, 76.7466)

    @Before
    fun setUp() {
        stopKoin()
        dataSource = FakeAndroidTestDataSource()
        appContext = getApplicationContext()

        val myModel = module {
            viewModel {
                RemindersListViewModel(appContext, dataSource as ReminderDataSource)
            }
        }

        startKoin { modules(listOf(myModel)) }

        runBlocking {
            dataSource.deleteAllReminders()
            dataSource.saveReminder(reminder1)
            dataSource.saveReminder(reminder2)
        }
    }

    @Test
    fun clickFab_navigateToSelection() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        Thread.sleep(1500)

        onView(withId(R.id.addReminderFAB)).perform(click())
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }

    @Test
    fun launchRemainderDetails_detailsVisible() {
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.reminderssRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<DataBindingViewHolder<ReminderDataItem>>(
                0
            )
        )

        onView(withText(reminder1.title)).check(matches(isDisplayed()))
        onView(withText(reminder1.location)).check(matches(isDisplayed()))
        onView(withText(reminder1.description)).check(matches(isDisplayed()))
    }

    @Test
    fun throwError_snackBarDisplayed() {
        (dataSource as FakeAndroidTestDataSource).shouldReturnError = true

        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Test Exception")))
    }
}
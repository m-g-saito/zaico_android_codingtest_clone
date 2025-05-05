package jp.co.zaico.codingtest

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.zaico.codingtest.di.ApiModule
import jp.co.zaico.codingtest.ui.inventorylist.InventoryListFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@UninstallModules(ApiModule::class)
@RunWith(AndroidJUnit4::class)
class InventoryListFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testInventoryListDisplayed() {
        launchFragmentInHiltContainer<InventoryListFragment>(
            themeResId = R.style.Theme_ZaicoCodingtest
        )

        onView(withText("データ1")).check(matches(isDisplayed()))
        onView(withText("データ2")).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchFiltersResults() {
        launchFragmentInHiltContainer<InventoryListFragment>(
            themeResId = R.style.Theme_ZaicoCodingtest
        )

        onView(withId(R.id.searchView)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(
            replaceText("データ1"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("データ1"))))
        onView(withText("データ2")).check(doesNotExist())
    }
}
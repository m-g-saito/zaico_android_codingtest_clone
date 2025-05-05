package jp.co.zaico.codingtest

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.zaico.codingtest.di.ApiModule
import jp.co.zaico.codingtest.model.InventoryResponse
import jp.co.zaico.codingtest.ui.inventorydetail.InventoryDetailFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@UninstallModules(ApiModule::class)
@RunWith(AndroidJUnit4::class)
class InventoryDetailFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testInventoryDetailsDisplayed() {
        val inventory = InventoryResponse(id = 1, title = "データ1", quantity = "10")

        val bundle = Bundle().apply {
            putParcelable("inventory", inventory)
        }
        launchFragmentInHiltContainer<InventoryDetailFragment>(
            themeResId = R.style.Theme_ZaicoCodingtest,
            fragmentArgs = bundle
        )

        onView(withId(R.id.textViewId)).check(matches(withText("1")))
        onView(withId(R.id.textViewTitle)).check(matches(withText("データ1")))
        onView(withId(R.id.textViewQuantity)).check(matches(withText("10")))
    }
}
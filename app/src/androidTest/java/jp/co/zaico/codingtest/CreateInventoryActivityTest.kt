package jp.co.zaico.codingtest

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jp.co.zaico.codingtest.ui.createinventory.CreateInventoryActivity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CreateInventoryActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var scenario: ActivityScenario<CreateInventoryActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        scenario = ActivityScenario.launch(
            CreateInventoryActivity.Companion.createIntent(ApplicationProvider.getApplicationContext())
        )
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun whenFieldsAreEmpty_submitDoesNotFinishActivity() {
        onView(withId(R.id.buttonSubmit)).perform(click())
        scenario.onActivity { activity ->
            Assert.assertFalse(activity.isFinishing)
        }
    }

    @Test
    fun whenFieldsAreValid_activityFinishesAfterSubmit() {
        onView(withId(R.id.editTextTitle)).perform(replaceText("データ1"), closeSoftKeyboard())
        onView(withId(R.id.editTextQuantity)).perform(replaceText("10"), closeSoftKeyboard())
        onView(withId(R.id.buttonSubmit)).perform(click())

        scenario.onActivity { activity ->
            assert(activity.isFinishing)
        }
    }

    @Test
    fun submitButton_isEnabledInitially() {
        onView(withId(R.id.buttonSubmit)).check(matches(isEnabled()))
    }

    @Test
    fun loadingOverlay_isGoneInitially() {
        onView(withId(R.id.loadingOverlay)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}
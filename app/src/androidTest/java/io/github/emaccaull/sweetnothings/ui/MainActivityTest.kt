/*
 * Copyright (C) 2018 Emmanuel MacCaull
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.emaccaull.sweetnothings.ui

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.emaccaull.sweetnothings.MoreIntentMatchers
import io.github.emaccaull.sweetnothings.R
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.data.InMemoryMessageDataSource
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java)
    private lateinit var messageDataSource: InMemoryMessageDataSource

    @Before
    fun setUp() {
        // DataSource is configured by TestSweetNothingsApp
        messageDataSource =
            activityRule.activity.configuration.messageDataSource() as InMemoryMessageDataSource
    }

    @After
    fun tearDown() {
        messageDataSource.clear()
    }

    @Test
    fun fragmentContainer_isVisible() {
        onView(ViewMatchers.withId(R.id.fragment_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun selectingGenerate_thenSend_sharesMessage() {
        // Given that there is a sweet nothing available
        val message = builder("xyz").message("<3 u").used(false).build()
        messageDataSource.add(message)

        // When the search button is clicked
        onView(ViewMatchers.withId(R.id.search_button)).perform(ViewActions.click())

        // Then we should have the option of sending the sweet nothing
        onView(ViewMatchers.withId(R.id.message_content))
            .check(ViewAssertions.matches(ViewMatchers.withText("<3 u")))

        // Stub out the share action...
        withStubbedShareAction()

        // When selecting Send
        onView(ViewMatchers.withId(R.id.send_button)).perform(ViewActions.click())

        // Then a share intent should be sent
        Intents.intended(MoreIntentMatchers.hasShareIntent("<3 u"))

        // And the sweet nothing should be marked as used
        val used = messageDataSource.fetchMessage("xyz").blockingGet()
        MatcherAssert.assertThat(used.isUsed, Matchers.`is`(true))
    }

    @Test
    fun sendButton_isDisabled_whenInitialInputIsEmpty() {
        // Given that the text box is empty
        onView(ViewMatchers.withId(R.id.message_content))
            .check(ViewAssertions.matches(ViewMatchers.withText("")))

        // Then the send button should be disabled
        onView(ViewMatchers.withId(R.id.send_button))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun sendButton_isDisabled_whenModifiedInputIsEmpty() {
        // Given that there is a sweet nothing available
        val message = builder("acai").message("<3 u").used(false).build()
        messageDataSource.add(message)
        onView(ViewMatchers.withId(R.id.search_button))
            .perform(ViewActions.click()) // Load the sweet nothing

        // Then the button should be enabled
        onView(ViewMatchers.withId(R.id.send_button))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        // After the text has been edited
        onView(ViewMatchers.withId(R.id.message_content)).perform(ViewActions.clearText())

        // Then the send button should be disabled
        onView(ViewMatchers.withId(R.id.send_button))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun selectingGenerate_whenNoMessagesAvailable_notifiesUser() {
        // Given that no sweet nothings are available (only used one present)
        val message = builder("abc").message("<3 u").used(true).build()
        messageDataSource.add(message)

        // When the generate button is clicked
        onView(ViewMatchers.withId(R.id.search_button)).perform(ViewActions.click())

        // Then a message should tell the user that they will have to check back later
        onView(ViewMatchers.withText(R.string.generate_failed_message_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.generate_failed_message_body))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.ok)).perform(ViewActions.click())
    }

    private fun withStubbedShareAction() {
        val shareResult = ActivityResult(Activity.RESULT_OK, null)
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_CHOOSER)).respondWith(shareResult)
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_SEND)).respondWith(shareResult)
    }
}
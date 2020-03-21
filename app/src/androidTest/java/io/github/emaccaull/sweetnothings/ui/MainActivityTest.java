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

package io.github.emaccaull.sweetnothings.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import io.github.emaccaull.sweetnothings.MoreIntentMatchers;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.data.InMemoryMessageDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class);

    private InMemoryMessageDataSource messageDataSource;

    @Before
    public void setUp() {
        // DataSource is configured by TestSweetNothingsApp
        messageDataSource =
                (InMemoryMessageDataSource)
                        activityRule.getActivity().getConfiguration().messageDataSource();
    }

    @After
    public void tearDown() {
        messageDataSource.clear();
    }

    @Test
    public void fragmentContainer_isVisible() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingGenerate_thenSend_sharesMessage() {
        // Given that there is a sweet nothing available
        SweetNothing message = SweetNothing.builder("xyz").message("<3 u").used(false).build();
        messageDataSource.add(message);

        // When the search button is clicked
        onView(withId(R.id.search_button)).perform(click());

        // Then we should have the option of sending the sweet nothing
        onView(withId(R.id.message_content)).check(matches(withText("<3 u")));

        // Stub out the share action...
        withStubbedShareAction();

        // When selecting Send
        onView(withId(R.id.send_button)).perform(click());

        // Then a share intent should be sent
        intended(MoreIntentMatchers.hasShareIntent("<3 u"));

        // And the sweet nothing should be marked as used
        SweetNothing used = messageDataSource.fetchMessage("xyz").blockingGet();
        assertThat(used.isUsed(), is(true));
    }

    @Test
    public void sendButton_isDisabled_whenInitialInputIsEmpty() {
        // Given that the text box is empty
        onView(withId(R.id.message_content)).check(matches(withText("")));

        // Then the send button should be disabled
        onView(withId(R.id.send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void sendButton_isDisabled_whenModifiedInputIsEmpty() {
        // Given that there is a sweet nothing available
        SweetNothing message = SweetNothing.builder("acai").message("<3 u").used(false).build();
        messageDataSource.add(message);
        onView(withId(R.id.search_button)).perform(click()); // Load the sweet nothing

        // Then the button should be enabled
        onView(withId(R.id.send_button)).check(matches(isEnabled()));

        // After the text has been edited
        onView(withId(R.id.message_content)).perform(clearText());

        // Then the send button should be disabled
        onView(withId(R.id.send_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void selectingGenerate_whenNoMessagesAvailable_notifiesUser() {
        // Given that no sweet nothings are available (only used one present)
        SweetNothing message = SweetNothing.builder("abc").message("<3 u").used(true).build();
        messageDataSource.add(message);

        // When the generate button is clicked
        onView(withId(R.id.search_button)).perform(click());

        // Then a message should tell the user that they will have to check back later
        onView(withText(R.string.generate_failed_message_title)).check(matches(isDisplayed()));
        onView(withText(R.string.generate_failed_message_body)).check(matches(isDisplayed()));
        onView(withText(R.string.ok)).perform(click());
    }

    private void withStubbedShareAction() {
        Instrumentation.ActivityResult shareResult =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(shareResult);
        intending(hasAction(Intent.ACTION_SEND)).respondWith(shareResult);
    }
}

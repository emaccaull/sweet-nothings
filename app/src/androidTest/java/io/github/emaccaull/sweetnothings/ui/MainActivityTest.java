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
import android.content.pm.ActivityInfo;
import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import io.github.emaccaull.sweetnothings.MoreIntentMatchers;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.data.FakeMessageDataSource;
import io.github.emaccaull.sweetnothings.glue.Glue;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class);

    private FakeMessageDataSource fakeMessageDataSource;

    @Before
    public void setUp() {
        // DataSource is configured by TestSweetNothingsApp
        fakeMessageDataSource = (FakeMessageDataSource) Glue.provideMessageDataSource();
    }

    @After
    public void tearDown() {
        fakeMessageDataSource.clear();
    }

    @Test
    public void fragmentContainer_isVisible() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingGenerate_thenSend_sharesMessage() {
        onView(withText(R.string.generate_found_message_title)).check(doesNotExist());
        onView(withText(R.string.generate_send)).check(doesNotExist());
        onView(withText(R.string.cancel)).check(doesNotExist());

        // Given that there is a sweet nothing available
        SweetNothing message = SweetNothing.builder("xyz").message("<3 u").used(false).build();
        fakeMessageDataSource.insert(message);

        // When the generate button is clicked
        onView(withId(R.id.generate_phrase_btn)).perform(click());

        // Then we should have the option of sending the sweet nothing
        onView(withText(R.string.generate_found_message_title)).check(matches(isDisplayed()));
        onView(withText("<3 u")).check(matches(isDisplayed()));

        // Stub out the share action...
        withStubbedShareAction();

        // When selecting Send
        onView(withText(R.string.generate_send)).perform(click());

        // Then a share intent should be sent
        intended(MoreIntentMatchers.hasShareIntent("<3 u"));

        // And the sweet nothing should be marked as used
        SweetNothing used = fakeMessageDataSource.fetchMessage("xyz").blockingGet();
        assertThat(used.isUsed(), is(true));
    }

    @Test
    public void selectingGenerate_thenCancel_dismissesDialog() {
        // Given that there is a sweet nothing available
        SweetNothing message = SweetNothing.builder("xyz").message("<3 u").used(false).build();
        fakeMessageDataSource.insert(message);

        // And the generate button is clicked
        onView(withId(R.id.generate_phrase_btn)).perform(click());

        // When cancel is clicked
        onView(withText(R.string.cancel)).perform(click());

        // Then the dialog should be dismissed
        onView(withText(R.string.generate_found_message_title)).check(doesNotExist());

        // Even after rotation
        FragmentActivity activity = activityRule.getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withText(R.string.generate_found_message_title)).check(doesNotExist());
    }

    @Test
    public void selectingGenerate_whenNoMessagesAvailable_notifiesUser() {
        // Given that no sweet nothings are available (only used one present)
        SweetNothing message = SweetNothing.builder("abc").message("<3 u").used(true).build();
        fakeMessageDataSource.insert(message);

        // When the generate button is clicked
        onView(withId(R.id.generate_phrase_btn)).perform(click());

        // Then a message should tell the user that they will have to check back later
        onView(withText(R.string.generate_found_message_title)).check(doesNotExist());
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

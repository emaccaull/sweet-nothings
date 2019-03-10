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

package io.github.emaccaull.sweetnothings;

import android.content.Intent;
import android.os.Bundle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static org.hamcrest.Matchers.allOf;

/**
 * Custom Intent matchers that don't ship with Espresso.
 */
public final class MoreIntentMatchers {

    private MoreIntentMatchers() {}

    /**
     * Matches an intent to share plain text.
     */
    public static Matcher<Intent> hasShareIntent(String text) {
        return new TypeSafeMatcher<Intent>() {
            @Override
            protected boolean matchesSafely(Intent intent) {
                if (intent.getAction() == null
                        || !intent.getAction().equals(Intent.ACTION_CHOOSER)) {
                    return false;
                }

                Bundle extras = intent.getExtras();
                if (extras == null) {
                    return false;
                }

                Intent shareIntent = extras.getParcelable(Intent.EXTRA_INTENT);
                if (shareIntent == null) {
                    return false;
                }

                return allOf(
                                hasAction(Intent.ACTION_SEND),
                                hasExtra(Intent.EXTRA_TEXT, text),
                                hasType("text/plain"))
                        .matches(shareIntent);
            }

            @Override
            public void describeTo(Description description) {}
        };
    }
}

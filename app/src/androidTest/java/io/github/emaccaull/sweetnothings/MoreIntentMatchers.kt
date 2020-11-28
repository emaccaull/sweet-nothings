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
package io.github.emaccaull.sweetnothings

import android.content.Intent
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

/**
 * Custom Intent matchers that don't ship with Espresso.
 */
object MoreIntentMatchers {

    /**
     * Matches an intent to share plain text.
     */
    fun hasShareIntent(text: String): Matcher<Intent> {
        return object : TypeSafeMatcher<Intent>() {
            override fun matchesSafely(intent: Intent): Boolean {
                if (intent.action == null || intent.action != Intent.ACTION_CHOOSER) {
                    return false
                }
                val extras = intent.extras ?: return false
                val shareIntent = extras.getParcelable<Intent>(Intent.EXTRA_INTENT) ?: return false
                return Matchers.allOf(
                    IntentMatchers.hasAction(Intent.ACTION_SEND),
                    IntentMatchers.hasExtra(Intent.EXTRA_TEXT, text),
                    IntentMatchers.hasType("text/plain")
                ).matches(shareIntent)
            }

            override fun describeTo(description: Description) {}
        }
    }
}

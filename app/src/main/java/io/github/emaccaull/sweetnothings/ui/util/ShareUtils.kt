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
package io.github.emaccaull.sweetnothings.ui.util

import android.app.Activity
import android.content.Intent
import androidx.core.app.ShareCompat
import io.github.emaccaull.sweetnothings.R

/**
 * Utilities for sharing data with other applications.
 */
object ShareUtils {
    /**
     * Creates an intent used to share the text contained in `messageBody` with another
     * application.
     *
     * @return a valid [Intent] or null if there is no receiver for the intent.
     */
    fun createShareIntent(launchingActivity: Activity, messageBody: CharSequence?): Intent? {
        val shareIntent = ShareCompat.IntentBuilder.from(launchingActivity)
            .setType("text/plain")
            .setText(messageBody)
            .setChooserTitle(R.string.generate_send_title)
            .createChooserIntent()
        return if (shareIntent.resolveActivity(launchingActivity.packageManager) != null) shareIntent else null
    }
}

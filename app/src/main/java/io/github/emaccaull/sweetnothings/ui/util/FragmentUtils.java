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

package io.github.emaccaull.sweetnothings.ui.util;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Utilities for managing Fragments.
 */
public final class FragmentUtils {

    private FragmentUtils() {
        throw new AssertionError("Cannot instantiate");
    }

    /**
     * Adds {@code fragment} to the {@code fragmentManager} using the view ID {@code
     * containerViewId}.
     */
    public static void replace(FragmentManager fragmentManager, Fragment fragment,
            @IdRes int containerViewId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }

    /**
     * Removes any fragments with the given {@code tag}, then shows {@code dialog} with the given
     * {@code tag}.
     */
    public static void showDialog(FragmentManager fragmentManager, DialogFragment dialog,
            @Nullable String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        dialog.show(fragmentManager, tag);
    }
}
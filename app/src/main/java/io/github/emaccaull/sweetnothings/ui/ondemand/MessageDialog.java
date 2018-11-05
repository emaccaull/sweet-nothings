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

package io.github.emaccaull.sweetnothings.ui.ondemand;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import io.github.emaccaull.sweetnothings.R;

/**
 * Allows a user to choose an action for a given sweet nothing.
 */
public class MessageDialog extends DialogFragment {
    private static final String ARG_TITLE_ID = "title";
    private static final String ARG_MESSAGE = "body";

    public static MessageDialog newInstance(@StringRes int titleId, CharSequence message) {
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE_ID, titleId);
        args.putCharSequence(ARG_MESSAGE, message);

        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            return createMessageDialog(
                    args.getInt(ARG_TITLE_ID),
                    args.getCharSequence(ARG_MESSAGE));
        }
        throw new AssertionError("No arguments specified");
    }

    private Dialog createMessageDialog(@StringRes int titleId, CharSequence message) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(titleId)
                .setMessage(message)
                .setPositiveButton(R.string.generate_send, (a, b) -> { })
                .setNegativeButton(R.string.cancel, (a, b) -> { })
                .create();
    }
}

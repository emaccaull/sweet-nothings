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

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.ui.framework.BaseDialogFragment;

/**
 * Dialog for displaying a generic message to the user where the only action is to dismiss the
 * dialog.
 */
public class InformationalDialog extends BaseDialogFragment {
    private static final String ARG_TITLE_ID = "title_id";
    private static final String ARG_MESSAGE_ID = "message_id";

    public static InformationalDialog newInstance(
            @StringRes int titleId, @StringRes int messageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE_ID, titleId);
        args.putInt(ARG_MESSAGE_ID, messageId);

        InformationalDialog dialog = new InformationalDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = requireArguments();
        return new AlertDialog.Builder(requireContext())
                .setTitle(args.getInt(ARG_TITLE_ID))
                .setMessage(args.getInt(ARG_MESSAGE_ID))
                .setPositiveButton(R.string.ok, (dialog, which) -> {})
                .create();
    }
}

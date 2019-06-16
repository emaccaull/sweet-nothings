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
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import io.github.emaccaull.sweetnothings.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows a user to choose an action for a given sweet nothing.
 */
public class MessageDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String ARG_MESSAGE_ID = "message_id";
    private static final String ARG_TITLE_ID = "title";
    private static final String ARG_MESSAGE = "body";

    public interface MessageSharedListener {

        void onShareMessage(String messageId, CharSequence message);

        void onCancelled();
    }

    public static <T extends Fragment & MessageSharedListener> MessageDialog newInstance(
            String messageId, CharSequence message, @StringRes int titleId, T target) {
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE_ID, messageId);
        args.putInt(ARG_TITLE_ID, titleId);
        args.putCharSequence(ARG_MESSAGE, message);

        MessageDialog dialog = new MessageDialog();
        dialog.setArguments(args);
        dialog.setTargetFragment(checkNotNull(target, "target is null"), 0);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = requireArguments();
        return createMessageDialog(
                args.getInt(ARG_TITLE_ID),
                args.getCharSequence(ARG_MESSAGE));
    }

    private Dialog createMessageDialog(@StringRes int titleId, CharSequence message) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(titleId)
                .setMessage(message)
                .setPositiveButton(R.string.generate_send, this)
                .setNegativeButton(R.string.cancel, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Fragment f = getTargetFragment();
        if (!(f instanceof MessageSharedListener)) {
            throw new AssertionError(
                    "Target fragment " + f + " does not implement " + MessageSharedListener.class);
        }

        MessageSharedListener listener = (MessageSharedListener) f;
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE: {
                Bundle args = requireArguments();
                String messageId = args.getString(ARG_MESSAGE_ID);
                CharSequence message = args.getCharSequence(ARG_MESSAGE);
                listener.onShareMessage(messageId, message);
                break;
            }

            case DialogInterface.BUTTON_NEGATIVE: {
                listener.onCancelled();
                break;
            }

            default:
                throw new AssertionError("Unhandled button " + which);
        }
    }
}

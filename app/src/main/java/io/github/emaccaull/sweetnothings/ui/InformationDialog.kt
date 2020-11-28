/*
 * Copyright (C) 2020 Emmanuel MacCaull
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

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import io.github.emaccaull.sweetnothings.R

/**
 * Dialog for displaying a generic message to the user where the only action is to dismiss the
 * dialog.
 */
class InformationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireArguments().let { args ->
            AlertDialog.Builder(requireContext())
                .setTitle(args.getInt(ARG_TITLE_ID))
                .setMessage(args.getInt(ARG_MESSAGE_ID))
                .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int -> }
                .create()
        }
    }

    companion object {
        private const val ARG_TITLE_ID = "title_id"
        private const val ARG_MESSAGE_ID = "message_id"

        fun newInstance(@StringRes titleId: Int, @StringRes messageId: Int): InformationDialog =
            InformationDialog().apply {
                arguments = bundleOf(ARG_TITLE_ID to titleId, ARG_MESSAGE_ID to messageId)
            }
    }
}

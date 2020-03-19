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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.app.SweetNothingsApp;
import io.github.emaccaull.sweetnothings.ui.InformationDialog;
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils;
import io.github.emaccaull.sweetnothings.ui.util.ShareUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
public class GeneratorFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorFragment.class);

    private static final String CONFIRMATION_TAG = "ui.ondemand.confirm";
    private static final String APOLOGY_TAG = "ui.ondemand.notfound";

    private GeneratorViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.message_content)
    EditText messageContent;

    @BindView(R.id.search_button)
    Button searchButton;

    private Unbinder unbinder;

    public static GeneratorFragment newInstance() {
        return new GeneratorFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        logger.debug("Attached to {}", context.getClass().getSimpleName());

        OnDemandBuilder.ParentComponent component =
                ((SweetNothingsApp) context.getApplicationContext()).getConfiguration();
        new OnDemandBuilder(component).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generator_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        viewModel = obtainViewModel();
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::updateViewState);
        viewModel.requestInitialMessage();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.search_button)
    void onSearchClicked(View view) {
        viewModel.requestNewMessage();
    }

    @OnClick(R.id.send_button)
    void onSendClicked(View view) {
        CharSequence text = messageContent.getText();
        if (text != null) {
            onShareMessage(text.toString());
        }
    }

    private GeneratorViewModel obtainViewModel() {
        return new ViewModelProvider(this, viewModelFactory).get(GeneratorViewModel.class);
    }

    private void updateViewState(ViewState state) {
        searchButton.setEnabled(!state.isLoading());

        if (state.getSweetNothing() != null) {
            messageContent.setText(state.getSweetNothing().getMessage());
        } else if (state.isNotFound()) {
            apologize();
        }
    }

    private void onShareMessage(String message) {
        Intent shareIntent = ShareUtils.createShareIntent(requireActivity(), message);
        if (shareIntent != null) {
            startActivity(shareIntent);
            viewModel.onShareSuccessful(message);
        } else {
            viewModel.onShareFailed(message);
        }
    }

    /// Apologies

    private void apologize() {
        InformationDialog dialog = InformationDialog.newInstance(
                R.string.generate_failed_message_title, R.string.generate_failed_message_body);
        FragmentUtils.showDialog(getParentFragmentManager(), dialog, APOLOGY_TAG);
    }
}

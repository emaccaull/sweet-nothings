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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent;

import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.app.SweetNothingsApp;
import io.github.emaccaull.sweetnothings.databinding.GeneratorFragmentBinding;
import io.github.emaccaull.sweetnothings.ui.InformationDialog;
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils;
import io.github.emaccaull.sweetnothings.ui.util.ShareUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
public class GeneratorFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorFragment.class);

    private static final String APOLOGY_TAG = "ui.ondemand.notfound";

    private final CompositeDisposable viewDisposables = new CompositeDisposable();

    private GeneratorViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private GeneratorFragmentBinding binding;

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
        binding = GeneratorFragmentBinding.inflate(inflater, container, false);

        binding.searchButton.setOnClickListener(this::onSearchClicked);
        binding.sendButton.setOnClickListener(this::onSendClicked);

        Disposable d =
                RxTextView.afterTextChangeEvents(binding.messageContent)
                        .map(TextViewAfterTextChangeEvent::getEditable)
                        .subscribe(this::onMessageContentChanged);
        viewDisposables.add(d);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = obtainViewModel();
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::updateViewState);
        viewModel.requestInitialMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewDisposables.clear();
        binding = null;
    }

    private void onSearchClicked(View view) {
        viewModel.requestNewMessage();
    }

    private void onSendClicked(View view) {
        CharSequence text = binding.messageContent.getText();
        if (text != null) {
            onShareMessage(text.toString());
        }
    }

    private void onMessageContentChanged(CharSequence text) {
        // TODO: is there a way to manage this state from the ViewModel?
        binding.sendButton.setEnabled(!TextUtils.isEmpty(text));
    }

    private GeneratorViewModel obtainViewModel() {
        return new ViewModelProvider(this, viewModelFactory).get(GeneratorViewModel.class);
    }

    private void updateViewState(ViewState state) {
        binding.searchButton.setEnabled(!state.isLoading());

        if (state.isNotFound()) {
            apologize();
        } else if (state.getSweetNothing() != null) {
            binding.messageContent.setText(state.getSweetNothing().getMessage());
        } else {
            // Initial state
            binding.messageContent.setText(null);
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

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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.emaccaull.sweetnothings.R;
import io.github.emaccaull.sweetnothings.databinding.GeneratorFragmentBinding;
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
public class GeneratorFragment extends Fragment {
    private static final String CONFIRMATION_TAG = "ui.ondemand.confirm";

    private final Logger logger = LoggerFactory.getLogger(GeneratorFragment.class);

    private GeneratorFragmentBinding binding;
    private GeneratorViewModel viewModel;

    public static GeneratorFragment newInstance() {
        return new GeneratorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logger.debug("Attached");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.generator_fragment, container, false);
        binding.generatePhraseBtn.setOnClickListener(this::onGenerateClicked);

        viewModel = obtainViewModel();
        viewModel.getViewState().observe(getViewLifecycleOwner(), this::updateViewState);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private GeneratorViewModel obtainViewModel() {
        GeneratorViewModelFactory factory = new GeneratorViewModelFactory();
        return ViewModelProviders.of(this, factory).get(GeneratorViewModel.class);
    }

    private void updateViewState(ViewState state) {
        binding.generatePhraseBtn.setEnabled(!state.isLoading());

        if (state.getMessage() != null) {
            confirmSend(state.getMessage());
        }
    }

    private void confirmSend(String message) {
        MessageDialog dialog = MessageDialog.newInstance(
                R.string.generate_found_message_title, message);

        FragmentUtils.showDialog(requireFragmentManager(), dialog, CONFIRMATION_TAG);
    }

    public void onGenerateClicked(View view) {
        viewModel.requestNewMessage();
    }
}

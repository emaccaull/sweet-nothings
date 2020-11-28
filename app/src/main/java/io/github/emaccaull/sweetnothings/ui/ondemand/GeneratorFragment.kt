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
package io.github.emaccaull.sweetnothings.ui.ondemand

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import io.github.emaccaull.sweetnothings.R
import io.github.emaccaull.sweetnothings.app.SweetNothingsApp
import io.github.emaccaull.sweetnothings.databinding.GeneratorFragmentBinding
import io.github.emaccaull.sweetnothings.ui.InformationDialog
import io.github.emaccaull.sweetnothings.ui.ondemand.OnDemandBuilder.ParentComponent
import io.github.emaccaull.sweetnothings.ui.util.FragmentUtils
import io.github.emaccaull.sweetnothings.ui.util.ShareUtils
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Generate/fetch a Random SweetNothing on demand.
 */
class GeneratorFragment : Fragment() {
    private val viewDisposables = CompositeDisposable()
    private lateinit var viewModel: GeneratorViewModel

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private var _binding: GeneratorFragmentBinding? = null
    /** Only valid between onCreateView and onDestroyView. */
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logger.debug("Attached to {}", context.javaClass.simpleName)
        val component: ParentComponent =
            (context.applicationContext as SweetNothingsApp).configuration
        OnDemandBuilder(component).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GeneratorFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            searchButton.setOnClickListener { view: View -> onSearchClicked(view) }
            sendButton.setOnClickListener { view: View -> onSendClicked(view) }
            val d = messageContent.afterTextChangeEvents()
                .map { editable -> editable.editable ?: "" }
                .subscribe { text: CharSequence -> onMessageContentChanged(text) }
            viewDisposables.add(d)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = obtainViewModel()
        viewModel.viewState.observe(viewLifecycleOwner) { state -> updateViewState(state) }
        viewModel.requestInitialMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDisposables.clear()
        _binding = null
    }

    private fun onSearchClicked(view: View) {
        viewModel.requestNewMessage()
    }

    private fun onSendClicked(view: View) {
        val text: CharSequence? = binding.messageContent.text
        if (text != null) {
            onShareMessage(text.toString())
        }
    }

    private fun onMessageContentChanged(text: CharSequence) {
        // TODO: is there a way to manage this state from the ViewModel?
        binding.sendButton.isEnabled = !TextUtils.isEmpty(text)
    }

    private fun obtainViewModel(): GeneratorViewModel {
        return ViewModelProvider(this, viewModelFactory).get(GeneratorViewModel::class.java)
    }

    private fun updateViewState(state: ViewState) {
        binding.searchButton.isEnabled = !state.isLoading
        when {
            state.isNotFound -> {
                apologize()
            }
            state.sweetNothing != null -> {
                binding.messageContent.setText(state.sweetNothing!!.message)
            }
            else -> {
                // Initial state
                binding.messageContent.text = null
            }
        }
    }

    private fun onShareMessage(message: String) {
        val shareIntent = ShareUtils.createShareIntent(requireActivity(), message)
        if (shareIntent != null) {
            startActivity(shareIntent)
            viewModel.onShareSuccessful(message)
        } else {
            viewModel.onShareFailed(message)
        }
    }

    /// Apologies
    private fun apologize() {
        val dialog = InformationDialog.newInstance(
            R.string.generate_failed_message_title, R.string.generate_failed_message_body
        )
        FragmentUtils.showDialog(parentFragmentManager, dialog, APOLOGY_TAG)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GeneratorFragment::class.java)
        private const val APOLOGY_TAG = "ui.ondemand.notfound"

        @JvmStatic
        fun newInstance(): GeneratorFragment {
            return GeneratorFragment()
        }
    }
}
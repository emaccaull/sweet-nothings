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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.core.SchedulerProvider
import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.core.TrampolineSchedulerProvider
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GeneratorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRandomSweetNothing = mockk<GetRandomSweetNothing>()
    private val markUsed = mockk<MarkUsed>()
    private val observer = mockk<Observer<ViewState>>(relaxed = true)

    private val schedulerProvider: SchedulerProvider = TrampolineSchedulerProvider
    private lateinit var viewModel: GeneratorViewModel

    @Before
    fun setUp() {
        viewModel = GeneratorViewModel(schedulerProvider, getRandomSweetNothing, markUsed)
        viewModel.viewState.observeForever(observer)
    }

    @Test
    fun init_noStreamsOpen() {
        // When a new instance is created, then it shouldn't have any RxJava streams open
        assertThat(viewModel.subscriberCount()).isEqualTo(0)
    }

    // When a new ViewModel is created and it has an observer, then the observer should get its
    // view contents from the ViewModel
    @Test
    fun viewState_whenSubscribed_emitsDefaultViewState() {
        // When a new ViewModel is created and it has an observer, then the observer should get its
        // view contents from the ViewModel
        verify { observer.onChanged(ViewState.initial()) }
    }

    @Test
    fun requestInitialMessage() {
        val sn = withRandomSweetNothing("$()$")

        // When requesting an initial sweet nothing
        viewModel.requestInitialMessage()

        // Then the view should be updated
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.loaded(sn))
        }

        verify(exactly = 0) {
            observer.onChanged(ViewState.noMessageFound())
        }
   }

    @Test
    fun requestInitialSweetNothing_whenNoMessageAvailable() {
        withNoSweetNothing()

        // When requesting a sweet nothing and none are available
        viewModel.requestInitialMessage()

        // Then the view should updated with an empty message
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.initial())
        }

        verify(exactly = 0) {
            observer.onChanged(ViewState.noMessageFound())
        }
    }

    @Test
    fun requestNewMessage_registersDisposable() {
        withRandomSweetNothing("<3<3")

        // When requesting a new message
        viewModel.requestNewMessage()

        // Then a disposable should be added to the list
        assertThat(viewModel.subscriberCount()).isEqualTo(1)
    }

    @Test
    fun requestNewMessage_whenAnItemIsFound_showsMessage() {
        val sn = withRandomSweetNothing("<3<3")

        // When requesting a new sweet nothing
        viewModel.requestNewMessage()

        // Then the view should get the appropriate update
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.loaded(sn))
        }

        verify(exactly = 0) {
            observer.onChanged(ViewState.noMessageFound())
        }
    }

    @Test
    fun requestNewMessage_whenNoItemCouldBeFound_showsError() {
        withNoSweetNothing()

        // When requesting a new sweet nothing
        viewModel.requestNewMessage()

        // Then the view should display an error
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.noMessageFound())
        }
    }

    @Test
    fun requestNewMessage_onError_showsError() {
        withErrorSweetNothing()

        // When requesting a new sweet nothing
        viewModel.requestNewMessage()

        // Then the view should display an error
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.noMessageFound())
        }
    }

    @Test
    fun sendMessage() {
        every { markUsed.apply(any()) } returns Completable.complete()

        // Given that a message was requested
        val sn = withRandomSweetNothing("snId", "Some Message <3")
        viewModel.requestNewMessage()

        // When the user has sent the message
        viewModel.onShareSuccessful("Some Message <3")

        // Then the sweet nothing should be marked as used
        verify {
            markUsed.apply("Some Message <3")
        }

        // And the view state should be reset
        verifyOrder {
            observer.onChanged(ViewState.loaded(sn))
            observer.onChanged(ViewState.initial())
        }
    }

    @Test
    fun shareFailed() {
        // Given that a message was requested
        val sn = withRandomSweetNothing("snId", "Some Message <3")
        viewModel.requestNewMessage()

        // When sharing the message failed
        viewModel.onShareFailed("Some Message <3")

        // Then the sweet nothing should not be marked as used
        verify(exactly = 0) {
            markUsed.apply("Some Message <3")
        }

        // And the view state should be reset
        verifyOrder {
            observer.onChanged(ViewState.loaded(sn))
            observer.onChanged(ViewState.initial())
        }
    }

    @Test
    fun cancelSend() {
        // Given that a message was requested
        val sn = withRandomSweetNothing("<3!")
        viewModel.requestNewMessage()

        // When the user does not want to send the message
        viewModel.resetViewState()

        // Then the view state should be reset
        verifyOrder {
            observer.onChanged(ViewState.loading())
            observer.onChanged(ViewState.loaded(sn))
            observer.onChanged(ViewState.initial())
        }
    }

    private fun withRandomSweetNothing(message: String): SweetNothing {
        return withRandomSweetNothing("id1", message)
    }

    private fun withRandomSweetNothing(id: String, message: String): SweetNothing {
        val sweetNothing = builder(id).message(message).build()
        every { getRandomSweetNothing.apply() } returns Maybe.just(sweetNothing)
        return sweetNothing
    }

    private fun withNoSweetNothing() {
        every { getRandomSweetNothing.apply() } returns Maybe.empty()
    }

    private fun withErrorSweetNothing() {
        every { getRandomSweetNothing.apply() } returns Maybe.error(Exception())
    }
}

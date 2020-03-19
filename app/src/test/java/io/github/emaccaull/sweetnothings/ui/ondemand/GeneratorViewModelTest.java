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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import io.github.emaccaull.sweetnothings.core.SchedulerProvider;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.TrampolineSchedulerProvider;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetRandomSweetNothing getRandomSweetNothing;

    @Mock
    private MarkUsed markUsed;

    @Mock
    private Observer<ViewState> observer;

    private final SchedulerProvider schedulerProvider = TrampolineSchedulerProvider.INSTANCE;

    private GeneratorViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new GeneratorViewModel(schedulerProvider, getRandomSweetNothing, markUsed);
        viewModel.getViewState().observeForever(observer);
    }

    @Test
    public void init_noStreamsOpen() {
        // When a new instance is created, then it shouldn't have any RxJava streams open
        assertThat(viewModel.subscriberCount(), is(0));
    }

    @Test
    public void getViewState_whenSubscribed_emitsDefaultViewState() {
        // When a new ViewModel is created and it has an observer, then the observer should get its
        // view contents from the ViewModel
        verify(observer).onChanged(ViewState.initial());
    }

    @Test
    public void requestInitialMessage() {
        SweetNothing sn = withRandomSweetNothing("$()$");

        // When requesting an initial sweet nothing
        viewModel.requestInitialMessage();

        // Then the view should be updated
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.loaded(sn));

        verify(observer, never()).onChanged(ViewState.noMessageFound());
    }

    @Test
    public void requestInitialSweetNothing_whenNoMessageAvailable() {
        withNoSweetNothing();

        // When requesting a sweet nothing and none are available
        viewModel.requestInitialMessage();

        // Then the view should updated with an empty message
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.initial());

        verify(observer, never()).onChanged(ViewState.noMessageFound());
    }

    @Test
    public void requestNewMessage_registersDisposable() {
        withRandomSweetNothing("<3<3");

        // When requesting a new message
        viewModel.requestNewMessage();

        // Then a disposable should be added to the list
        assertThat(viewModel.subscriberCount(), is(1));
    }

    @Test
    public void requestNewMessage_whenAnItemIsFound_showsMessage() {
        SweetNothing sn = withRandomSweetNothing("<3<3");

        // When requesting a new sweet nothing
        viewModel.requestNewMessage();

        // Then the view should get the appropriate update
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.loaded(sn));

        verify(observer, never()).onChanged(ViewState.noMessageFound());
    }

    @Test
    public void requestNewMessage_whenNoItemCouldBeFound_showsError() {
        withNoSweetNothing();

        // When requesting a new sweet nothing
        viewModel.requestNewMessage();

        // Then the view should display an error
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.noMessageFound());
    }

    @Test
    public void requestNewMessage_onError_showsError() {
        withErrorSweetNothing();

        // When requesting a new sweet nothing
        viewModel.requestNewMessage();

        // Then the view should display an error
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.noMessageFound());
    }

    @Test
    public void sendMessage() {
        when(markUsed.apply(any())).thenReturn(Completable.complete());

        // Given that a message was requested
        SweetNothing sn = withRandomSweetNothing("snId", "Some Message <3");
        viewModel.requestNewMessage();

        // When the user has sent the message
        viewModel.onShareSuccessful("Some Message <3");

        // Then the sweet nothing should be marked as used
        verify(markUsed).apply("Some Message <3");

        // And the view state should be reset
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loaded(sn));
        inOrder.verify(observer).onChanged(ViewState.initial());
    }

    @Test
    public void shareFailed() {
        // Given that a message was requested
        SweetNothing sn = withRandomSweetNothing("snId", "Some Message <3");
        viewModel.requestNewMessage();

        // When sharing the message failed
        viewModel.onShareFailed("Some Message <3");

        // Then the sweet nothing should not be marked as used
        verify(markUsed, never()).apply("Some Message <3");

        // And the view state should be reset
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loaded(sn));
        inOrder.verify(observer).onChanged(ViewState.initial());
    }

    @Test
    public void cancelSend() {
        // Given that a message was requested
        SweetNothing sn = withRandomSweetNothing("<3!");
        viewModel.requestNewMessage();

        // When the user does not want to send the message
        viewModel.resetViewState();

        // Then the view state should be reset
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(ViewState.loading());
        inOrder.verify(observer).onChanged(ViewState.loaded(sn));
        inOrder.verify(observer).onChanged(ViewState.initial());
    }

    private SweetNothing withRandomSweetNothing(String message) {
        return withRandomSweetNothing("id1", message);
    }

    private SweetNothing withRandomSweetNothing(String id, String message) {
        SweetNothing sweetNothing = SweetNothing.builder(id).message(message).build();
        when(getRandomSweetNothing.apply()).thenReturn(Maybe.just(sweetNothing));
        return sweetNothing;
    }

    private void withNoSweetNothing() {
        when(getRandomSweetNothing.apply()).thenReturn(Maybe.empty());
    }

    private void withErrorSweetNothing() {
        when(getRandomSweetNothing.apply()).thenReturn(Maybe.error(new Exception()));
    }
}

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

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.reactivex.Maybe;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetRandomSweetNothing getRandomSweetNothing;

    @Mock
    private Observer<ViewState> observer;

    private GeneratorViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new GeneratorViewModel(getRandomSweetNothing);
        viewModel.getViewState().observeForever(observer);
    }

    @Test
    public void getViewState_whenSubscribed_emitsDefaultViewState() {
        // When a new ViewModel is created and it has an observer, then the observer should get its
        // view contents from the ViewModel
        verify(observer).onChanged(new ViewState(false, null, false));
    }

    @Test
    public void requestNewMessage_whenAnItemIsFound_showsMessage() {
        withRandomSweetNothing();

        // When requesting a new sweet nothing
        viewModel.requestNewMessage();

        // Then the view should get the appropriate update
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(new ViewState(true, null, false));
        inOrder.verify(observer).onChanged(new ViewState(false, "<3<3", false));
    }

    @Test
    public void requestNewMessage_whenNoItemCouldBeFound_showsError() {
        withNoSweetNothing();

        // When requesting a new sweet nothing
        viewModel.requestNewMessage();

        // Then the view should display an error
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(new ViewState(true, null, false));
        inOrder.verify(observer).onChanged(new ViewState(false, null, true));
    }

    private void withRandomSweetNothing() {
        SweetNothing sweetNothing = SweetNothing.builder("id1").message("<3<3").build();
        when(getRandomSweetNothing.apply()).thenReturn(Maybe.just(sweetNothing));
    }

    private void withNoSweetNothing() {
        when(getRandomSweetNothing.apply()).thenReturn(Maybe.empty());
    }
}

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
import android.arch.lifecycle.ViewModel;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.github.emaccaull.sweetnothings.core.usecase.MarkUsed;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorViewModelFactoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetRandomSweetNothing getRandomSweetNothing;

    @Mock
    private MarkUsed markUsed;

    @Test
    public void create() {
        GeneratorViewModelFactory factory =
                new GeneratorViewModelFactory(getRandomSweetNothing, markUsed);

        GeneratorViewModel viewModel = factory.create(GeneratorViewModel.class);
        assertThat(viewModel, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_whenInvalidModelClass() {
        GeneratorViewModelFactory factory =
                new GeneratorViewModelFactory(getRandomSweetNothing, markUsed);

        factory.create(ViewModel.class);
    }
}

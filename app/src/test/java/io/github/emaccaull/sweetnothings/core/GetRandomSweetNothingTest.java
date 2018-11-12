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

package io.github.emaccaull.sweetnothings.core;

import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import io.github.emaccaull.sweetnothings.core.usecase.GetRandomSweetNothing;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetRandomSweetNothingTest {

    @Mock
    private MessageDataSource messageDataSource;

    private GetRandomSweetNothing getRandomSweetNothing;

    @Before
    public void setUp() {
        getRandomSweetNothing = new GetRandomSweetNothing(messageDataSource);
    }

    @Test
    public void apply() {
        // Given that the data source will return a SweetNothing for our filter
        MessageFilter filter = MessageFilter.builder().includeUsed(false).build();
        SweetNothing message = SweetNothing.builder("id123").message("howdy").build();
        when(messageDataSource.fetchRandomMessage(filter)).thenReturn(Maybe.just(message));

        // When applying the use case
        TestObserver<SweetNothing> observer =
                getRandomSweetNothing.apply().test();

        // Then the observer should have received the given sweet nothing
        observer.assertValue(message);
    }
}

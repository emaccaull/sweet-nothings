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

package io.github.emaccaull.sweetnothings.core.usecase;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.MessageDataSource;
import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetSweetNothingTest {

    @Mock
    private MessageDataSource messageDataSource;

    @Test
    public void apply() {
        // Given that the data source will return a sweet nothing for a given ID
        SweetNothing sweetNothing = SweetNothing.builder("Id01").message("<3").build();
        when(messageDataSource.fetchMessage("Id01")).thenReturn(Maybe.just(sweetNothing));

        // Given that we have a sweet nothing
        GetSweetNothing getSweetNothing = new GetSweetNothing(messageDataSource);

        // When the use case is applied
        TestObserver<SweetNothing> observer = getSweetNothing.apply("Id01").test();

        // Then the sweet nothing should be received
        observer.assertValue(sweetNothing);
    }
}

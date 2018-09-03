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

package io.github.emaccaull.sweetnothings.data;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryTest {

    @Mock
    private MessageDataSource localDataSource;

    private MessageRepository repository;

    private TestObserver<MessageEntry> observer = new TestObserver<>();

    @Before
    public void setUp() {
        repository = new MessageRepository(localDataSource);
    }

    @Test
    public void getRandomMessage() {
        // Given
        MessageEntry message =
                new MessageEntry("foo", false, false);
        when(localDataSource.fetchRandomMessage()).thenReturn(Maybe.just(message));

        // When retrieving a random message
        repository.getRandomMessage().subscribe(observer);

        // Then only the non-blacklisted message should be returned.
        observer.assertComplete();
        observer.assertValueCount(1);
        assertThat(observer.values().get(0).getMessage(), is("foo"));
    }

    @Test
    public void getRandomMessage_whenLocalEmpty_fetchesFromNetwork() {
        // TODO
    }
}

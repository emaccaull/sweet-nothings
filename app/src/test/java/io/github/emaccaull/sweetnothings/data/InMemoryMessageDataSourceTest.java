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

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryMessageDataSourceTest {

    private InMemoryMessageDataSource dataSource = new InMemoryMessageDataSource();

    @Test
    public void fetchRandomMessage() {
        // Given that there is one SweetNothing available
        SweetNothing message = SweetNothing.builder("abc").message("hello").build();
        dataSource.insertImmediate(message);

        // When retrieving a random sweet nothing
        dataSource.fetchRandomMessage(MessageFilter.selectAll())
                // Then the inserted sweet nothing should be delivered
                .test()
                .assertValue(message);
    }

    @Test
    public void fetchRandomMessage_whenMessages() {
        // Given that there are no sweet nothings

        // When requesting a random one
        dataSource.fetchRandomMessage(MessageFilter.selectAll())
                // Then an empty maybe should be returned
                .test()
                .assertNoValues()
                .assertComplete();
    }

    @Test
    public void fetchRandomMessage_whenAllItemsUsed() {
        // Given that there are no unused sweet nothings
        SweetNothing message = SweetNothing.builder("abc").message("hello").used(true).build();
        dataSource.insertImmediate(message);

        // When requesting non-blacklisted items
        dataSource.fetchRandomMessage(MessageFilter.builder().includeUsed(false).build())
                // Then an empty maybe should be returned
                .test()
                .assertNoValues()
                .assertComplete();
    }

    @Test
    public void fetchMessage() {
        // Given that there is a sweet nothing with the given id
        SweetNothing message = SweetNothing.builder("ID").message("foo").build();
        dataSource.insertImmediate(message);

        // When fetching the message
        dataSource.fetchMessage("ID")
                // Then the message should be returned
                .test()
                .assertValue(message);
    }

    @Test
    public void fetchMessage_whenNotPresent() {
        // When asking for a message that is not present
        dataSource.fetchMessage("1211")
                // Then the result should complete without an emission
                .test()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void markUsed() {
        // Given that there is a sweet nothing with the given id
        SweetNothing message = SweetNothing.builder("1234").message("foo").build();
        dataSource.insertImmediate(message);

        // When marking the message as used
        dataSource.markUsed("1234").test().assertComplete();

        // Then the message should be modified
        SweetNothing modified = dataSource.fetchMessage("1234").blockingGet();
        assertTrue(modified.isUsed());
    }

    @Test
    public void insert() {
        // Given that we have a new message to insert
        String message = "Some sweet text";

        // When adding it to storage
        SweetNothing sweetNothing = dataSource.insert(message).blockingGet();

        // Then a sweet nothing with the text should be returned
        assertThat(sweetNothing.getId(), is(notNullValue()));
        assertThat(sweetNothing.getMessage(), is(message));
        assertThat(sweetNothing.isBlacklisted(), is(false));
        assertThat(sweetNothing.isUsed(), is(false));
    }

    @Test
    public void size() {
        // When the db is empty, the size should be 0
        dataSource.size().test().assertValue(0);

        // When there is an item
        SweetNothing message = SweetNothing.builder("4321").message("oof").build();
        dataSource.insertImmediate(message);

        // Then the size should be 1
        dataSource.size().test().assertValue(1);
    }

    @Test
    public void clear() {
        // Given that there is an item in the dataSource
        SweetNothing message = SweetNothing.builder("756").message("foo").build();
        dataSource.insertImmediate(message);

        // When clearing it
        dataSource.clear();

        // Then that item shouldn't be stored
        SweetNothing sweetNothing = dataSource.fetchMessage("756").blockingGet();
        assertThat(sweetNothing, is(nullValue()));
    }
}

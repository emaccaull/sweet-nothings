/*
 * Copyright (C) 2019 Emmanuel MacCaull
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

package io.github.emaccaull.sweetnothings.data.local;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import io.github.emaccaull.sweetnothings.core.SweetNothing;
import io.github.emaccaull.sweetnothings.core.data.MessageFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class LocalMessageDataSourceTest {

    private LocalMessageDataSource dataSource;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        MessagesDatabase.switchToInMemory(context);

        dataSource = new LocalMessageDataSource(MessagesDatabase.getInstance(context));
    }

    @Test
    public void fetchRandom() {
        // Given that a particular sweet nothing exists in the db
        SweetNothing added = SweetNothing.builder("XYQ123").message("hello").build();
        dataSource.add(added);

        // When selecting a random sweet nothing
        MessageFilter filter = MessageFilter.selectAll();
        SweetNothing message = dataSource
                .fetchRandomMessage(filter)
                .blockingGet();

        // Then a sweet nothing should be retrieved
        assertThat(message, is(notNullValue()));
    }

    @Test
    public void fetchRandom_whenAllItemsUsed() {
        // Given that a used sweet nothing exists in the db
        SweetNothing added = SweetNothing.builder("XYQ123").message("hello").used(true).build();
        dataSource.add(added);

        // When selecting a random but excluding used
        MessageFilter filter = MessageFilter.builder().includeUsed(false).build();
        SweetNothing message = dataSource.fetchRandomMessage(filter).blockingGet();

        // Then no sweet nothing should be found
        assertThat(message, is(nullValue()));
    }

    @Test
    public void fetchMessage() {
        // Given that a particular sweet nothing exists in the db
        SweetNothing added = SweetNothing.builder("1234").message("foo").build();
        dataSource.add(added);

        // When fetching that item
        SweetNothing retrieved = dataSource.fetchMessage("1234").blockingGet();

        // Then it should be the message that was added
        assertThat(retrieved, is(added));
    }

    @Test
    public void search() {
        // Given that there is a sweet nothing with an exact content match
        SweetNothing added = SweetNothing.builder("id123").message("i <3 u").build();
        dataSource.add(added);

        // When search for that item
        SweetNothing retrieved = dataSource.search("i <3 u").blockingGet();

        // Then it should be the message that was added
        assertThat(retrieved, is(added));
    }

    @Test
    public void search_whenNoMatchPresent() {
        // Given that a partial match is present
        SweetNothing added = SweetNothing.builder("id789").message("some content").build();
        dataSource.add(added);

        // When searching for that item with a partial match
        SweetNothing retrieved = dataSource.search("some").blockingGet();

        // Then not items are found
        assertThat(retrieved, is(nullValue()));
    }

    @Test
    public void markUsed() {
        // Given that a sweet nothing exists in the db
        SweetNothing added = SweetNothing.builder("ABC123").message("<3").build();
        dataSource.add(added);

        // When marking that item as used
        dataSource.markUsed("ABC123").blockingAwait();

        // Then subsequent queries should see that item as used
        SweetNothing retrieved = dataSource.fetchMessage("ABC123").blockingGet();
        assertThat(retrieved, is(notNullValue()));
        assertThat(retrieved.isUsed(), is(true));
    }
}

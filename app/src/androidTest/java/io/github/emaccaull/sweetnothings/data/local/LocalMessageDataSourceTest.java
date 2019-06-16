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
import io.github.emaccaull.sweetnothings.data.internal.Ids;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LocalMessageDataSourceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Ids ids;

    private LocalMessageDataSource dataSource;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        MessagesDatabase.switchToInMemory(context);

        dataSource = new LocalMessageDataSource(context, ids);
    }

    @Test
    public void fetchRandom() {
        // Given that a particular sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("XYQ123").message("hello").build();
        dataSource.insertImmediate(inserted);

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
        SweetNothing inserted = SweetNothing.builder("XYQ123").message("hello").used(true).build();
        dataSource.insertImmediate(inserted);

        // When selecting a random but excluding used
        MessageFilter filter = MessageFilter.builder().includeUsed(false).build();
        SweetNothing message = dataSource.fetchRandomMessage(filter).blockingGet();

        // Then no sweet nothing should be found
        assertThat(message, is(nullValue()));
    }

    @Test
    public void fetchMessage() {
        // Given that a particular sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("1234").message("foo").build();
        dataSource.insertImmediate(inserted);

        // When fetching that item
        SweetNothing retrieved = dataSource.fetchMessage("1234").blockingGet();

        // Then it should be the message that was inserted
        assertThat(retrieved, is(inserted));
    }

    @Test
    public void markUsed() {
        // Given that a sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("ABC123").message("<3").build();
        dataSource.insertImmediate(inserted);

        // When marking that item as used
        dataSource.markUsed("ABC123").subscribe();

        // Then subsequent queries should see that item as used
        SweetNothing retrieved = dataSource.fetchMessage("ABC123").blockingGet();
        assertThat(retrieved, is(notNullValue()));
        assertThat(retrieved.isUsed(), is(true));
    }

    @Test
    public void insert() {
        when(ids.nextUuid()).thenReturn("uuid1");

        // Given that we have a new message to insert
        String message = "Some sweet text";

        // When adding it to storage
        SweetNothing sweetNothing = dataSource.insert(message).blockingGet();

        // Then a sweet nothing with the text should be returned
        assertThat(sweetNothing.getId(), is("uuid1"));
        assertThat(sweetNothing.getMessage(), is(message));
        assertThat(sweetNothing.isBlacklisted(), is(false));
        assertThat(sweetNothing.isUsed(), is(false));
    }

    @Test
    public void insertIfNotPresent() {
        when(ids.nextUuid()).thenReturn("idXYZ");

        // Given that there is a sweet nothing in the database
        String existing = "A message";
        SweetNothing sweetNothing = dataSource.insert(existing).blockingGet();
        assertThat(sweetNothing, is(notNullValue()));

        // When adding new items and one is a duplicate
        List<SweetNothing> sweetNothings =
                dataSource.insertIfNotPresent("Something", existing, "hello!").blockingGet();

        // Then 2 of the three should have been add
        assertThat(sweetNothings, hasSize(2));
        assertThat(sweetNothings.get(0).getMessage(), is("Something"));
        assertThat(sweetNothings.get(1).getMessage(), is("hello!"));
    }

    @Test
    public void size() {
        when(ids.nextUuid()).thenReturn("ID");

        // Size should be zero when the db is empty.
        dataSource.size().test().assertValue(0);

        // Given that a sweet nothing exists in the db
        dataSource.insert("<><>").subscribe();

        // When asking for the size
        dataSource.size().test().assertValue(1);
    }
}

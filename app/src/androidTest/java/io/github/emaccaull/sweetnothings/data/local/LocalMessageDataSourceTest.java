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

@RunWith(AndroidJUnit4.class)
public class LocalMessageDataSourceTest {

    static class TestIds extends Ids {
        String nextUuid;

        @Override
        String nextUuid() {
            return nextUuid;
        }
    }

    private TestIds ids = new TestIds();

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
        dataSource.insert(inserted).subscribe();

        // When selecting a random sweet nothing
        MessageFilter filter = MessageFilter.selectAll();
        SweetNothing message = dataSource
                .fetchRandomMessage(filter)
                .blockingGet();

        // Then a sweet nothing should be retrieved
        assertThat(message, is(notNullValue()));
    }

    @Test
    public void fetchMessage() {
        // Given that a particular sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("1234").message("foo").build();
        dataSource.insert(inserted).subscribe();

        // When fetching that item
        SweetNothing retrieved = dataSource.fetchMessage("1234").blockingGet();

        // Then it should be the message that was inserted
        assertThat(retrieved, is(inserted));
    }

    @Test
    public void markUsed() {
        // Given that a sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("ABC123").message("<3").build();
        dataSource.insert(inserted).subscribe();

        // When marking that item as used
        dataSource.markUsed("ABC123").subscribe();

        // Then subsequent queries should see that item as used
        SweetNothing retrieved = dataSource.fetchMessage("ABC123").blockingGet();
        assertThat(retrieved, is(notNullValue()));
        assertThat(retrieved.isUsed(), is(true));
    }

    @Test
    public void insert() {
        ids.nextUuid = "uuid1";

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
    public void size() {
        // Size should be zero when the db is empty.
        dataSource.size().test().assertValue(0);

        // Given that a sweet nothing exists in the db
        SweetNothing inserted = SweetNothing.builder("ABC123").message("<><>").build();
        dataSource.insert(inserted).subscribe();

        // When asking for the size
        dataSource.size().test().assertValue(1);
    }
}

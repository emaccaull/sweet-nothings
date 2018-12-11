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

package io.github.emaccaull.sweetnothings.data.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
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

    private LocalMessageDataSource dataSource;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        MessagesDatabase.switchToInMemory(context);

        dataSource = new LocalMessageDataSource(context);
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
}

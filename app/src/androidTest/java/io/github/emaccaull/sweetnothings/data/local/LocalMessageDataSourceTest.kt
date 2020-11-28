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
package io.github.emaccaull.sweetnothings.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import io.github.emaccaull.sweetnothings.core.data.MessageFilter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalMessageDataSourceTest {

    private lateinit var dataSource: LocalMessageDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        MessagesDatabase.switchToInMemory(context)
        dataSource = LocalMessageDataSource(MessagesDatabase.getInstance(context))
    }

    @Test
    fun fetchRandom() {
        // Given that a particular sweet nothing exists in the db
        val added = builder("XYQ123").message("hello").build()
        dataSource.add(added)

        // When selecting a random sweet nothing
        val filter = MessageFilter.selectAll()
        val message = dataSource
            .fetchRandomMessage(filter)
            .blockingGet()

        // Then a sweet nothing should be retrieved
        assertThat(message, `is`(notNullValue()))
    }

    @Test
    fun fetchRandom_whenAllItemsUsed() {
        // Given that a used sweet nothing exists in the db
        val added = builder("XYQ123").message("hello").used(true).build()
        dataSource.add(added)

        // When selecting a random but excluding used
        val filter = MessageFilter(false)
        val message = dataSource.fetchRandomMessage(filter).blockingGet()

        // Then no sweet nothing should be found
        assertThat(message, `is`(nullValue()))
    }

    @Test
    fun fetchMessage() {
        // Given that a particular sweet nothing exists in the db
        val added = builder("1234").message("foo").build()
        dataSource.add(added)

        // When fetching that item
        val retrieved = dataSource.fetchMessage("1234").blockingGet()

        // Then it should be the message that was added
        assertThat(retrieved, `is`(added))
    }

    @Test
    fun search() {
        // Given that there is a sweet nothing with an exact content match
        val added = builder("id123").message("i <3 u").build()
        dataSource.add(added)

        // When search for that item
        val retrieved = dataSource.search("i <3 u").blockingGet()

        // Then it should be the message that was added
        assertThat(retrieved, `is`(added))
    }

    @Test
    fun search_whenNoMatchPresent() {
        // Given that a partial match is present
        val added = builder("id789").message("some content").build()
        dataSource.add(added)

        // When searching for that item with a partial match
        val retrieved = dataSource.search("some").blockingGet()

        // Then not items are found
        assertThat(retrieved, `is`(nullValue()))
    }

    @Test
    fun markUsed() {
        // Given that a sweet nothing exists in the db
        val added = builder("ABC123").message("<3").build()
        dataSource.add(added)

        // When marking that item as used
        dataSource.markUsed("ABC123").blockingAwait()

        // Then subsequent queries should see that item as used
        val retrieved = dataSource.fetchMessage("ABC123").blockingGet()
        assertThat(retrieved, `is`(notNullValue()))
        assertThat(retrieved.isUsed, `is`(true))
    }
}

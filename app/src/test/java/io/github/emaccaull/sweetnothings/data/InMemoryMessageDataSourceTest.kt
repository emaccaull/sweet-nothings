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
package io.github.emaccaull.sweetnothings.data

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.BaseTestFixture
import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.data.MessageFilter
import org.junit.Assert
import org.junit.Test

class InMemoryMessageDataSourceTest : BaseTestFixture() {
    private val dataSource = InMemoryMessageDataSource()

    @Test
    fun fetchRandomMessage() {
        // Given that there is one SweetNothing available
        val message = SweetNothing.builder("abc").message("hello").build()
        dataSource.add(message)

        // When retrieving a random sweet nothing
        dataSource.fetchRandomMessage(MessageFilter.selectAll()) // Then the added sweet nothing should be delivered
            .test()
            .assertValue(message)
    }

    @Test
    fun fetchRandomMessage_whenMessages() {
        // Given that there are no sweet nothings

        // When requesting a random one
        dataSource.fetchRandomMessage(MessageFilter.selectAll()) // Then an empty maybe should be returned
            .test()
            .assertNoValues()
            .assertComplete()
    }

    @Test
    fun fetchRandomMessage_whenAllItemsUsed() {
        // Given that there are no unused sweet nothings
        val message = SweetNothing.builder("abc").message("hello").used(true).build()
        dataSource.add(message)

        // When requesting non-blacklisted items
        val filter = MessageFilter(includeUsed = false)
        dataSource.fetchRandomMessage(filter) // Then an empty maybe should be returned
            .test()
            .assertNoValues()
            .assertComplete()
    }

    @Test
    fun fetchMessage() {
        // Given that there is a sweet nothing with the given id
        val message = SweetNothing.builder("ID").message("foo").build()
        dataSource.add(message)

        // When fetching the message
        dataSource.fetchMessage("ID") // Then the message should be returned
            .test()
            .assertValue(message)
    }

    @Test
    fun fetchMessage_whenNotPresent() {
        // When asking for a message that is not present
        dataSource.fetchMessage("1211") // Then the result should complete without an emission
            .test()
            .assertComplete()
            .assertNoValues()
    }

    @Test
    fun search() {
        // Given that there is a sweet nothing with the given message
        val sweetNothing = SweetNothing.builder("ID").message("miss u").build()
        dataSource.add(sweetNothing)

        // When searching for the message
        dataSource.search("miss u") // Then the sweet nothing should be returned
            .test()
            .assertComplete()
            .assertValue(sweetNothing)
    }

    @Test
    fun search_whenNoMatchFound() {
        // When searching for a message that is not present
        dataSource.search("something") // Then nothing should be found
            .test()
            .assertComplete()
            .assertNoValues()
    }

    @Test
    fun markUsed() {
        // Given that there is a sweet nothing with the given id
        val message = SweetNothing.builder("1234").message("foo").build()
        dataSource.add(message)

        // When marking the message as used
        dataSource.markUsed("1234").test().assertComplete()

        // Then the message should be modified
        val modified = dataSource.fetchMessage("1234").blockingGet()
        Assert.assertTrue(modified.isUsed)
    }

    @Test
    fun clear() {
        // Given that there is an item in the dataSource
        val message = SweetNothing.builder("756").message("foo").build()
        dataSource.add(message)

        // When clearing it
        dataSource.clear()

        // Then that item shouldn't be stored
        val sweetNothing = dataSource.fetchMessage("756").blockingGet()
        assertThat(sweetNothing).isNull()
    }
}

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
package io.github.emaccaull.sweetnothings.core.data

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test
import java.util.*

class AbstractMessageDataSourceTest {

    private val ids = mockk<Ids>()
    private val dataSource = TestMessageDataSource(ids)

    @Test
    fun create() {
        every { ids.nextUuid() } returns "uuid1"

        // Given that we have a new message to insert
        val message = "Some sweet text"

        // When adding it to storage
        val sweetNothing = dataSource.create(message).blockingGet()

        // Then a sweet nothing with the text should be returned
        assertThat(sweetNothing.id).isEqualTo("uuid1")
        assertThat(sweetNothing.message).isEqualTo(message)
        assertThat(sweetNothing.isBlacklisted).isFalse()
        assertThat(sweetNothing.isUsed).isFalse()
        assertThat(dataSource.existingMessages).contains(message)
    }

    @Test
    fun createIfNotPresent() {
        every { ids.nextUuid() } returns "idXYZ"

        // Given that there is a sweet nothing in the database
        val existing = "A message"
        val sweetNothing = dataSource.create(existing).blockingGet()
        assertThat(sweetNothing).isNotNull()

        // When adding new items and one is a duplicate
        val sweetNothings =
            dataSource.createIfNotPresent("Something", existing, "hello!").blockingGet()

        // Then 2 of the three should have been add
        assertThat(sweetNothings).hasSize(2)
        assertThat(sweetNothings[0].message).isEqualTo("Something")
        assertThat(sweetNothings[1].message).isEqualTo("hello!")
    }

    internal class TestMessageDataSource(ids: Ids) : AbstractMessageDataSource(ids) {
        private val _existingMessages: MutableSet<String> = HashSet()

        public override val existingMessages: Set<String> = _existingMessages

        override fun fetchRandomMessage(filter: MessageFilter): Maybe<SweetNothing> {
            return Maybe.empty()
        }

        override fun fetchMessage(id: String): Maybe<SweetNothing> {
            return Maybe.empty()
        }

        override fun search(exactMessage: String): Maybe<SweetNothing> {
            return Maybe.empty()
        }

        override fun markUsed(id: String): Completable {
            return Completable.complete()
        }

        override fun add(sweetNothing: SweetNothing) {
            _existingMessages.add(sweetNothing.message)
        }
    }
}

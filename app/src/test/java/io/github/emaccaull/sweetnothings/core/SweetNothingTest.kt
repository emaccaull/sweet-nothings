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
package io.github.emaccaull.sweetnothings.core

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.BaseTestFixture
import io.github.emaccaull.sweetnothings.core.SweetNothing
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SweetNothingTest : BaseTestFixture() {

    @Test
    fun message() {
        val sn = builder("9923").message("hi").build()
        assertThat(sn.message).isEqualTo("hi")
    }

    @Test(expected = IllegalArgumentException::class)
    fun builder_whenNullMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        builder("123").message(null).build()
        // Then an exception should be thrown
    }

    @Test(expected = IllegalArgumentException::class)
    fun builder_whenEmptyMessage_throwsException() {
        // When building a sweet nothing that doesn't have a message
        builder("6473")
            .message("")
            .build()
        // Then an exception should be thrown
    }

    @Test
    fun builder_canCopyAllData() {
        // Given that there is an existing sweet nothing
        val sn = builder("abc")
            .message("hi")
            .used(true)
            .blacklisted(true)
            .build()

        // When making a copy of the sweet nothing
        val snCopy = builder(sn).build()

        // Then the two should be equal
        assertThat(snCopy).isEqualTo(sn)
    }

    @Test
    fun id() {
        // When constructing SweetNothing with a valid ID
        val sn = builder("foobar").message("hello").build()

        // Then it should have that ID
        assertThat(sn.id).isEqualTo("foobar")
    }

    @Test
    fun isBlacklisted() {
        val sn = builder("aaa")
            .message("a")
            .blacklisted(true)
            .build()
        assertThat(sn.isBlacklisted).isTrue()
    }

    @Test
    fun isUsed() {
        val sn = builder("bbb")
            .message("b")
            .used(true)
            .build()
        assertThat(sn.isUsed).isTrue()
    }

    @Test
    fun equals() {
        EqualsVerifier.forClass(SweetNothing::class.java).verify()
    }
}

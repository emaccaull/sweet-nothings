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

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.core.SweetNothing.Companion.builder
import org.junit.Test

class MessageTest {

    @Test
    fun toSweetNothing() {
        val message = Message(0, "123", "testing", true, true)
        val sweetNothing = message.toSweetNothing()

        assertThat(sweetNothing.id).isEqualTo("123")
        assertThat(sweetNothing.message).isEqualTo("testing")
        assertThat(sweetNothing.isUsed).isEqualTo(true)
        assertThat(sweetNothing.isBlacklisted).isEqualTo(true)
    }

    @Test
    fun fromSweetNothing() {
        val sn = builder("foo")
            .message("hello").blacklisted(true).used(true)
            .build()

        messageOf(sn).apply {
            assertThat(uuid).isEqualTo("foo")
            assertThat(content).isEqualTo("hello")
            assertThat(isBlacklisted).isEqualTo(true)
            assertThat(isUsed).isEqualTo(true)
        }
    }
}

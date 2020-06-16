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
package io.github.emaccaull.sweetnothings.core.data

import com.google.common.truth.Truth.assertThat
import io.github.emaccaull.sweetnothings.BaseTestFixture
import io.github.emaccaull.sweetnothings.core.data.MessageFilter.Companion.builder
import io.github.emaccaull.sweetnothings.core.data.MessageFilter.Companion.selectAll
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class MessageFilterTest : BaseTestFixture() {

    @Test
    fun builder_defaultIncludesAll() {
        val filter = builder().build()
        assertThat(filter.includeUsed()).isTrue()
    }

    @Test
    fun builder_whenIgnoreUsed_filterDoesNotIncludeUsed() {
        // Given that we can create a builder that excludes used messages
        val builder = builder().includeUsed(false)

        // When constructing a MessageFilter from that builder
        val filter = builder.build()

        // Then the filter should also exclude used messages
        assertThat(filter.includeUsed()).isFalse()
    }

    @Test
    fun selectAll_IncludesAll() {
        val filter = selectAll()
        assertThat(filter.includeUsed()).isTrue()
    }

    @Test
    fun equals() {
        EqualsVerifier.forClass(MessageFilter::class.java).verify()
    }
}

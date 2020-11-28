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
import org.junit.Test

class MessageFilterTest : BaseTestFixture() {

    @Test
    fun defaultIncludesAll() {
        val filter = MessageFilter()
        assertThat(filter.includeUsed).isTrue()
    }

    @Test
    fun construct_whenIgnoreUsed_filterDoesNotIncludeUsed() {
        val filter = MessageFilter(includeUsed = false)
        assertThat(filter.includeUsed).isFalse()
    }

    @Test
    fun selectAll_IncludesAll() {
        val filter = MessageFilter.selectAll()
        assertThat(filter.includeUsed).isTrue()
    }
}
